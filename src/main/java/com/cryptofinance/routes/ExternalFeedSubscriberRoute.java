package com.cryptofinance.routes;

import com.cryptofinance.model.PriceRecord;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
public class ExternalFeedSubscriberRoute extends RouteBuilder {

    private static final String EXTERNAL_URL = "http://localhost:5000/btc-price";

    @Override
    public void configure() throws Exception {
        from("direct:get_btc_usd_price")
                .setHeader(Exchange.HTTP_METHOD, simple("GET"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to(EXTERNAL_URL)
                .unmarshal(new JacksonDataFormat(PriceRecord.class))
                .to("direct:btc_usd_price_source")
        ;
    }
}
