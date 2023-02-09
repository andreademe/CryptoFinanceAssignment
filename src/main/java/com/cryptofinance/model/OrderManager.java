package com.cryptofinance.model;

import com.cryptofinance.AccountRepository;
import com.cryptofinance.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.cryptofinance.model.OrderType.LIMIT;

@Component
@Slf4j
public class OrderManager {

    @Autowired
    public AccountRepository accountServiceBean;

    @Autowired
    public OrderRepository orderRepository;

    public Account saveAccount(Account account) {
        return accountServiceBean.saveAccount(account);
    }

    public Account getAccount(Long id) {
        return accountServiceBean.getAccount(id);
    }

    public Order addLimitOrder(Order order) {
        Order orderWithId = orderRepository.saveOrder(order);
        accountServiceBean.addLimitOrder(orderWithId);
        return order;
    }

    public Order getOrder(Long orderId) {
        return orderRepository.getOrder(orderId);
    }

    public void executeLimitOrders(PriceRecord priceRecord) {
        log.info("BTC-USD price as of {} is {}", priceRecord.getTimestamp(), priceRecord.getPrice());
        List<Order> executableOrders = orderRepository.getExecutableOrders(OrderType.LIMIT, priceRecord.getPrice());
        log.info("Following LIMIT orders can be executed: {}", executableOrders);

        for (Order order : executableOrders) {
            Account account = accountServiceBean.getAccount(order.getAccountId());
            log.info("Executing limit order {} on account {}", order, account);

            if (order.getOrderType() == LIMIT) {
                switch (order.getOrderSide()) {
                    case BUY: {
                        BigDecimal finalPriceUSD = priceRecord.getPrice().multiply(order.getAmount());
                        if (account.canBuy(finalPriceUSD)) {
                            account.subtractFromUsdBalance(finalPriceUSD);
                            account.addBtcAmount(order.getAmount());
                            order.setExecuted(true);
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
    }
}
