package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.UserNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM tenmo_account WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            BigDecimal balance = results.getBigDecimal("balance");
            return balance;
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public BigDecimal addTEBucks(BigDecimal addAmount, int userId) {
        Account account = findAccountById(userId);
        BigDecimal balanceAfterAdd = account.getBalance().add(addAmount);

        String sql = "UPDATE tenmo_account SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, balanceAfterAdd, userId);

        return account.getBalance();
    }

    @Override
    public BigDecimal subtractTEBucks(BigDecimal subtractAmount, int userId) {
        Account account = findAccountById(userId);
        BigDecimal balanceAfterSubtract = account.getBalance().subtract(subtractAmount);

        String sql = "UPDATE tenmo_account SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, balanceAfterSubtract, userId);

        return account.getBalance();
    }

    @Override
    public Account getUserById(int userId) {
        String sql = "SELECT * FROM tenmo_account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Account findAccountById(int id) {
        String sql = "SELECT * FROM tenmo_account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            throw new UserNotFoundException();
        }
    }


    private Account mapRowToAccount(SqlRowSet as) {
        Account account = new Account();
        account.setAccountId(as.getInt("account_id"));
        account.setUserId(as.getInt("user_id"));
        account.setBalance(as.getBigDecimal("balance"));
        return account;
    }


}
