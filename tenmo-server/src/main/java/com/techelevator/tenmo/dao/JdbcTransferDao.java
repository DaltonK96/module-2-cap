package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }

    //Retrieve all transfers for a user
    @Override
    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM tenmo_transfer " +
                "JOIN tenmo_account ON tenmo_transfer.account_from = tenmo_account.account_id " +
                "WHERE tenmo_account.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    //Retrieve pending transfer(s) for a user
    @Override
    public List<Transfer> getPendingTransfer(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM tenmo_transfer " +
                "JOIN tenmo_account ON tenmo_transfer.account_from = tenmo_account.account_id " +
                "WHERE transfer_status_id = 1 AND user_id = ?);";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    //Retrieve a transfer by id
    @Override
    public Transfer getTransferByID(int transferId) {
        String sql = "SELECT * " +
                "FROM tenmo_transfer " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public String sendMoney(int userFrom, int userTo, BigDecimal amount) {
        //Choose from a list of users to send money to
        //userDao.findAll(); (MOVE TO CLIENT)
        //Check userFrom != userTo

        BigDecimal currentBalance = accountDao.getBalance(userFrom);
        int balanceComparisonResult = amount.compareTo(currentBalance);

        if (userFrom == userTo) {
            return "Not allowed to send funds to oneself.";
            //Check account balance !< transfer amount && Check transfer amount !<= 0
        } else if (balanceComparisonResult > 0) {
            return "Not allowed to send less funds than available.";
        } else {
            //SQL Decrease userFrom balance amount by transfer amount
            accountDao.subtractTEBucks(amount, userFrom);
            //SQL Increase userTo balance amount by transfer amount
            accountDao.addTEBucks(amount, userTo);
            //Display status as Approved

                // get account number for sender
                int accountSender = accountDao.findAccountById(userFrom).getAccountId();
                // get account number for recipient
                int accountRecipient = accountDao.findAccountById(userTo).getAccountId();

            //Add transfer to tenmo_transfer
            String sqlStatus = "INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (2, 2, ?, ?, ?);";
            jdbcTemplate.update(sqlStatus, accountSender, accountRecipient, amount);
            //Display the userFrom, userTo, and transfer amount
            return "Transfer Successful";
        }
    }

    //Request money from another user
    @Override
    public String requestMoney(int userFrom, int userTo, BigDecimal amount) {
        //Choose from a list of users to send money to
        userDao.findAll();
        //Check userFrom != userTo
        if (userFrom == userTo) {
            return "Not allowed to request funds from oneself, you heathen.";
        //Check transfer amount !<= 0
        } else if (amount.compareTo(new BigDecimal("0.00")) < 0) {
            return "Not allowed to request zero or negative funds, you bitch.";
        } else {
            //Display status as Pending
            String sqlStatus = "INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (1, 1, ?, ?, ?);";
            jdbcTemplate.update(sqlStatus, userFrom, userTo, amount);
            return "Transfer Pending";
        }
    }


    //Update pending request to either approved or rejected
    @Override
    public String updatePendingRequest(int statusId, Transfer transfer) {
            if (statusId == 2) {
            //SQL for update status
            String sql = "UPDATE tenmo_transfer " +
                    "SET transfer_status_id = 2 " +
                    "WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, transfer.getTransferId());
            return "Update successful, request approved.";
        } else if (statusId == 3){
                //SQL for update status
                String sql = "UPDATE tenmo_transfer " +
                        "SET transfer_status_id = 3 " +
                        "WHERE transfer_id = ?;";
                jdbcTemplate.update(sql, transfer.getTransferId());
            return "Update successful, request rejected.";
        }
        return "Update failed.";
    }

    private Transfer mapRowToTransfer(SqlRowSet ts) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(ts.getInt("transfer_id"));
        transfer.setTransferTypeId(ts.getInt("transfer_type_id"));
        transfer.setTransferTypeDescription(ts.getString("transfer_type_desc"));
        transfer.setTransferStatusId(ts.getInt("transfer_status_id"));
        transfer.setTransferStatusDescription(ts.getString("transfer_status_desc"));
        transfer.setAmount(ts.getBigDecimal("amount"));
        transfer.setUserTo(ts.getString("user_to"));
        transfer.setUserFrom(ts.getString("user_from"));
        transfer.setAccountTo(ts.getInt("account_to"));
        transfer.setAccountFrom(ts.getInt("account_from"));
        return transfer;
    }

}