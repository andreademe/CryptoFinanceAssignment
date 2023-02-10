# CryptoFinanceAssignment

Simple RESTful API for creating accounts and BUY side limit orders on BTC-USD implemented with Apache Camel and Spring Boot. 

The application has three Camel routes automatically started when the application is started. 

## RestAPIRoute

This route defines the RESTful service's endpoints and operations for managing accounts and orders. 

The Web server implementing the REST service is *Undertow*.

Normally, Springboot would be automatically started with *Tomcat*, but it is prevented from starting by excluding it in Maven's configuration.

Root URL: *localhost:8080/camel/api/*

## ExternalFeedSubscriberRoute

This route fetches BTC-USD prices from another API (http://localhost:5000/btc-price) and sends them to a Camel Endpoint. 

## OrderExecutionRoute

This route reads the Camel Endpoint on which the BTC-USD prices are available and executes the limit orders based on the received price. 
