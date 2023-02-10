package com.cryptofinance.routes;

import com.cryptofinance.model.PriceRecord;
import com.cryptofinance.persistence.DataManager;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderExecutionRoute extends RouteBuilder {

    @Autowired
    private DataManager dataManager;

    @Override
    public void configure() throws Exception {
        from("timer://my_timer?fixedRate=true&period=1000")//fires every second
                .to("direct:get_btc_usd_price");

        from("direct:btc_usd_price_source")
                .log("BTC-USD price on feed is: ${body}")
                .process(camelExchange -> {
                    PriceRecord priceRecord = camelExchange.getIn().getBody(PriceRecord.class);

                    dataManager.executeLimitOrders("BTC-USD", priceRecord.getPrice());
                })
        ;
    }
}
