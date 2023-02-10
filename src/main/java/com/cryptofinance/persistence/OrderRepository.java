package com.cryptofinance.persistence;

import com.cryptofinance.model.Order;
import com.cryptofinance.model.OrderSide;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class OrderRepository {

    private static final AtomicLong orderIdCounter = new AtomicLong();

    private final List<Order> orders = new ArrayList<>();

    public Order saveOrder(Order order) {
        Long id = orderIdCounter.getAndIncrement();
        order.setOrderId(id);
        orders.add(order);

        return order;
    }

    public Order getOrder(Long id) {
        for (Order order : orders) {
            if (order.getOrderId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    /*
    BUY order can be executed if currentPrice <= limit
    SELL order can be executed if currentPrice >= limit
     */
    public List<Order> getExecutableOrders(String tradingSymbol, BigDecimal currentPrice) {
        Objects.requireNonNull(tradingSymbol);
        Objects.requireNonNull(currentPrice);

        List<Order> executableOrders = new ArrayList<>();
        for (Order order : orders) {
            if (tradingSymbol.equals(order.getTradingSymbol()) &&
                    !order.isExecuted() &&
                    (order.getOrderSide() == OrderSide.BUY && order.getPriceLimit().compareTo(currentPrice) >= 0
                            || order.getOrderSide() == OrderSide.SELL && order.getPriceLimit().compareTo(currentPrice) <= 0)
            ) {
                executableOrders.add(order);
            }
        }
        return executableOrders;
    }
}
