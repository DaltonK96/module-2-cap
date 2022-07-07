package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Retrieve all transfers for a user
    @Override
    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * " +
                    "FROM tenmo_transfer " +
                    "JOIN tenmo_account ON tenmo_transfer.account_from = tenmo_account.account_id " +
                    "WHERE tenmo_account.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
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
        //Check userFrom != userTo
        if (userFrom == userTo) {
            return "Not allowed to send funds to oneself, you heathen.";
        }
        //Check account balance !< transfer amount
        //Check transfer amount !<= 0
        //if (amount.compareTo(accountDao.getBalance(userFrom) == -1 &&))
        //Display the userFrom, userTo, and transfer amount
        //SQL Increase userTo balance amount by transfer amount
        //SQL Decrease userFrom balance amount by transfer amount
        //Display status as Approved

        return "";
    }

    @Override
    public String requestMoney(int userFrom, int userTo, BigDecimal amount) {
        //SQL Increase userTo balance amount by transfer amount if approved
        //SQL Decrease userFrom balance amount by transfer amount if approved
        //Display status as Pending
        return null;
    }

    //Update pending request to either approved or rejected
    @Override
    public String updatePendingRequest(int statusId, Transfer transfer) {
        //Check for pending status
        if (statusId == 1) {
            //Check that the userFrom has enough money
            //Check that the userFrom approves
            //SQL for update status
            String sql = "UPDATE tenmo_transfer " +
                    "SET transfer_status_id = ? " +
                    "WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfer.getTransferId());
            return "Update successful.";
        } else {
            return "Update failed.";
        }

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
