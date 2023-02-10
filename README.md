# CryptoFinanceAssignment

Simple RESTful API for creating accounts and BUY side limit orders on BTC-USD implemented with Apache Camel and Spring Boot. 

The application has three Camel routes automatically started when the application is started. 

## RestAPIRoute

This route defines the RESTful service's endpoints and operations for managing accounts and orders. 

The Web server implementing the REST service is *Undertow*.

Normally, Springboot would be automatically started with *Tomcat*, but it is prevented from starting by excluding it in Maven's configuration.

Root URL: *localhost:8080/camel/api/*

### Operations
* **createAccount(name, usd_balance)**: Creates an account on the application with 0 BTC. The account id is an auto-incremented Long. 

Example: Create an account with balance of 10000 USD

Request:
* URL: `localhost:8080/camel/api/createAccount`
* Body
```
{
    "name": "Test account name", 
    "usd_balance":"10000"
}
```

Response:
```
{
    "accountId": 0,
    "name": "Test account name",
    "usdBalance": 10000,
    "btcAmount": 0
}
```

* **fetchAccountDetails(account_id)**: Fetches account details. 

Example: Fetching details about the account with id 0

Request:
* URL: `localhost:8080/camel/api/fetchAccountDetails?account_id=1`

Response:
```
{
    "accountId": 0,
    "name": "Test account name",
    "usdBalance": 10000,
    "btcAmount": 0
}
```

* **createLimitOrder(account_id, price_limit, amount)**: Creates a limit order, waiting to be executed when the price limit is reached. The order id is an auto-incremented Long.

Example 1: Create a limit order for buying 2 BTC with price limit 3000 USD

Request: 
* URL: `localhost:8080/camel/api/createLimitOrder`
* Body:

```
{
    "account_id": "0",
    "price_limit":"3000",
    "amount":"2"
}
```

Response:
```
{
    "orderId": 0,
    "priceLimit": 3000,
    "amount": 2,
    "orderSide": "BUY",
    "tradingSymbol": "BTC-USD",
    "account": {
        "accountId": 0,
        "name": "Test account name",
        "usdBalance": 10000,
        "btcAmount": 0
    },
    "executed": false,
    "executionPrice": null
}
```

Example 2: Create an order on an account that does not exist

Request:
* URL: `localhost:8080/camel/api/createLimitOrder`
* Body:
```
{
    "account_id": "100",
    "price_limit":"3000",
    "amount":"2"
}
```

Response:
```
"Order could not be created!"
```

* **fetchOrderDetails(order_id)**: Fetches order details and status

Example: Fetch order details about order with id 0

Request:
* URL: `localhost:8080/camel/api/fetchOrderDetails?order_id=0`

Response:
```
{
    "orderId": 0,
    "priceLimit": 3000,
    "amount": 2,
    "orderSide": "BUY",
    "tradingSymbol": "BTC-USD",
    "account": {
        "accountId": 0,
        "name": "Test account name",
        "usdBalance": 4029.846540335448,
        "btcAmount": 2
    },
    "executed": true,
    "executionPrice": 2985.076729832276
}
```

## ExternalFeedSubscriberRoute

This route fetches BTC-USD prices from another API (http://localhost:5000/btc-price) and sends them to a Camel Endpoint. 

## OrderExecutionRoute

This route reads the Camel Endpoint on which the BTC-USD prices are available and executes the limit orders based on the received price. 
