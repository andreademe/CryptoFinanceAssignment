package com.cryptofinance.routes;

import com.cryptofinance.api.in.CreateAccountRequest;
import com.cryptofinance.api.in.CreateLimitOrderRequest;
import com.cryptofinance.model.Account;
import com.cryptofinance.model.Order;
import com.cryptofinance.persistence.DataManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestAPIRoute extends RouteBuilder {

    @Autowired
    private DataManager dataManager;

    @Override
    public void configure() throws Exception {
        //default path = /camel/*
        //default host = localhost
        //default port = 8080
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto);

        rest("/api")
                .consumes("application/json")
                .produces("application/json")
                .post("/createAccount").type(CreateAccountRequest.class).outType(Account.class).to("direct:create_account")
                .get("/fetchAccountDetails?account_id={account_id}").outType(Account.class).to("direct:fetch_account_details")
                .post("/createLimitOrder").type(CreateLimitOrderRequest.class).outType(Order.class).to("direct:create_limit_order")
                .get("/fetchOrderDetails?order_id={order_id}").outType(Order.class).to("direct:fetch_order_details")
        ;

        from("direct:create_account")
                .routeId("create_account_route_id")
                .log("Received request to create account: ${body}")
                .process(camelExchange -> {
                    CreateAccountRequest request = camelExchange.getIn().getBody(CreateAccountRequest.class);

                    Account account = dataManager.createAccount(request);
                    camelExchange.getIn().setBody(account);
                })
        ;

        from("direct:fetch_account_details")
                .routeId("fetch_account_details_route_id")
                .log("Received request to fetch account details for account_id = ${header.account_id}")
                .process(camelExchange -> {
                    Long accountId = Long.parseLong(camelExchange.getIn().getHeader("account_id", String.class));
                    camelExchange.getIn().setBody(dataManager.getAccount(accountId));
                })
        ;

        from("direct:create_limit_order")
                .routeId("create_limit_order_route_id")
                .log("Received request to create limit order: ${body}")
                .process(camelExchange -> {
                    CreateLimitOrderRequest request = camelExchange.getIn().getBody(CreateLimitOrderRequest.class);

                    Order order = dataManager.createLimitOrder(request);
                    if (order != null) {
                        camelExchange.getIn().setBody(order);
                    } else {
                        log.info("Order could not be created!");
                        camelExchange.getIn().setBody("Order could not be created!");
                    }
                })
        ;

        from("direct:fetch_order_details")
                .routeId("fetch_order_details_route_id")
                .log("Received request to fetch order details for order_id = ${header.order_id}")
                .process(camelExchange -> {
                    Long orderId = Long.parseLong(camelExchange.getIn().getHeader("order_id", String.class));
                    camelExchange.getIn().setBody(dataManager.getLimitOrder(orderId));
                })
        ;
    }
}
