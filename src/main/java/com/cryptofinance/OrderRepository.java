package com.cryptofinance;

import com.cryptofinance.model.Order;
import com.cryptofinance.model.OrderSide;
import com.cryptofinance.model.OrderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class OrderRepository {

    private static AtomicLong orderIdCounter = new AtomicLong();
    private Map<Long, Order> orders = new TreeMap<>();

    private Long getNewOrderId() {
        return orderIdCounter.getAndIncrement();
    }

    public Order saveOrder(Order order) {
        order.setOrderId(getNewOrderId());
        orders.put(order.getOrderId(), order);
        return order;
    }

    public Order getOrder(Long id) {
        return orders.get(id);
    }

    /*
    BUY order can be executed if currentPrice <= limit
    SELL order can be executed if currentPrice >= limit
     */
    public List<Order> getExecutableOrders(OrderType orderType, BigDecimal currentPrice) {
        List<Order> executableOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (orderType == OrderType.LIMIT && !order.isExecuted() &&
                    (order.getOrderSide() == OrderSide.BUY && order.getPriceLimit().compareTo(currentPrice) >= 0
                            || order.getOrderSide() == OrderSide.SELL && order.getPriceLimit().compareTo(currentPrice) <= 0)) {
                executableOrders.add(order);
            }
        }
        return executableOrders;
    }
}
