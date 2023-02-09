package com.cryptofinance;

import com.cryptofinance.model.Account;
import com.cryptofinance.model.Order;
import com.cryptofinance.model.OrderManager;
import com.cryptofinance.model.PriceRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CryptoFinanceHomeworkMainRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //default path is /camel/*
        //default host = localhost
        //default port = 8080
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto);

        rest("/api")
                .consumes("application/json")
                .produces("application/json")
                .post("/createAccount").type(Account.class).outType(Account.class).to("direct:create_account")
                .get("/fetchAccountDetails?account_id={account_id}").outType(Account.class).to("direct:fetch_account_details")
                .post("/createLimitOrder").type(Order.class).outType(Account.class).to("direct:create_limit_order")
                .get("/fetchOrderDetails?order_id={order_id}").outType(Order.class).to("direct:fetch_order_details")
        ;

        from("direct:create_account")
                .routeId("create_account_route_id")
                .log("Create account: ${body}")
                .bean(OrderManager.class, "saveAccount(${body})")
        ;

        from("direct:fetch_account_details")
                .routeId("fetch_account_details_route_id")
                .log("Fetch account details for id = ${header.account_id}")
                .bean(OrderManager.class, "getAccount(${header.account_id})")
        ;

        from("direct:create_limit_order")
                .routeId("create_limit_order_route_id")
                .log("Create limit order: ${body}")
                .bean(OrderManager.class, "addLimitOrder(${body})")
        ;

        from("direct:fetch_order_details")
                .routeId("fetch_order_details_route_id")
                .log("Fetch order details for order id = ${header.order_id}")
                .bean(OrderManager.class, "getOrder(${header.order_id})")
        ;

        from("timer://foo?repeatCount=50")
                .setHeader(Exchange.HTTP_METHOD, simple("GET"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("http://localhost:5000/btc-price")
                .unmarshal(new JacksonDataFormat(PriceRecord.class))
                .bean(OrderManager.class, "executeLimitOrders(${body})")
        ;
    }
}
