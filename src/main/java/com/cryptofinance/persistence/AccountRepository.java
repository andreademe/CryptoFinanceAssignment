package com.cryptofinance.persistence;

import com.cryptofinance.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class AccountRepository {

    private static AtomicLong accountIdCounter = new AtomicLong();

    private Map<Long, Account> accounts = new HashMap<>();

    public Account saveAccount(Account account) {
        Long id = accountIdCounter.getAndIncrement();
        account.setAccountId(id);
        accounts.put(id, account);
        return account;
    }

    public Account getAccount(Long accountId) {
        return accounts.get(accountId);
    }
}
