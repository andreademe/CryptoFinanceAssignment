package com.cryptofinance;

import com.cryptofinance.model.Account;
import com.cryptofinance.model.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AccountRepository {

    private static AtomicLong accountIdCounter = new AtomicLong();

    private Map<Long, Account> accounts = new TreeMap<>();

    public Account saveAccount(Account account) {
        Long id = accountIdCounter.getAndIncrement();
        account.setAccountId(id);
        accounts.put(id, account);
        return account;
    }

    public Account getAccount(Long accountId) {
        return accounts.get(accountId);
    }

    public void addLimitOrder(Order order) {
        Account account = getAccount(order.getAccountId());
        account.addLimitOrder(order);
    }
}
