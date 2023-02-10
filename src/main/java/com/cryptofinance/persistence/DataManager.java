package com.cryptofinance.persistence;

import com.cryptofinance.api.in.CreateAccountRequest;
import com.cryptofinance.api.in.CreateLimitOrderRequest;
import com.cryptofinance.model.Account;
import com.cryptofinance.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.cryptofinance.model.OrderSide.BUY;

@Component
@Slf4j
public class DataManager {

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public OrderRepository orderRepository;

    public Account createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setName(request.getName());
        account.setUsdBalance(request.getUsdBalance());

        return accountRepository.saveAccount(account);
    }

    public Account getAccount(Long id) {
        return accountRepository.getAccount(id);
    }

    public Order createLimitOrder(CreateLimitOrderRequest request) {
        Account account = accountRepository.getAccount(request.getAccountId());
        if (account != null) {
            Order order = new Order(request.getPriceLimit(), request.getAmount(), BUY, "BTC-USD", account);
            return orderRepository.saveOrder(order);
        }
        return null;
    }

    public Order getLimitOrder(Long orderId) {
        return orderRepository.getOrder(orderId);
    }

    public void executeLimitOrders(String tradingSymbol, BigDecimal price) {
        log.info("{}", Thread.currentThread().getName());

        List<Order> executableOrders = orderRepository.getExecutableOrders(tradingSymbol, price);
        for (Order order : executableOrders) {
            executeLimitOrder(order, price);
        }
    }

    private void executeLimitOrder(Order order, BigDecimal feedPrice) {
        Account account = order.getAccount();
        log.info("Executing limit order {} on account {}", order, account);

        switch (order.getOrderSide()) {
            case BUY: {
                BigDecimal finalPriceUSD = feedPrice.multiply(order.getAmount());
                if (account.affords(finalPriceUSD)) {
                    account.subtractFromUsdBalance(finalPriceUSD);
                    account.addBtcAmount(order.getAmount());
                    order.setExecuted(true);
                    order.setExecutionPrice(feedPrice);
                } else {
                    log.warn("Account balance {} USD is below {} USD needed for buying {} BTC!", account.getUsdBalance(), finalPriceUSD, order.getAmount());
                }
                break;
            }
            case SELL: {
                log.error("Not implemented yet!");
                break;
            }
            default: {
                break;
            }
        }
        log.info("Executed limit order {} on account {}", order, account);
    }
}
