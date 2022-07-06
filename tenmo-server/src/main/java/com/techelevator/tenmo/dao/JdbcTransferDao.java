package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;
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


    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select * from tenmo_transfer;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;

    }

    @Override
    public List<Transfer> getPendingTransfer() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "select transfer_status_id from tenmo_transfer where ;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;

    }


    @Override
    public Transfer getTransferByID(int userId) {
        return null;
    }

    @Override
    public String updatePendingRequest(int userId, Transfer transfer) {
        return null;
    }

    @Override
    public String sendMoney(int userFrom, int userTo, BigDecimal amount) {
        return null;
    }

    @Override
    public String requestMoney(int userFrom, int userTo, BigDecimal amount) {
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet ts) {
        Transfer transfer = new Transfer();
        transfer.setAmount(ts.getBigDecimal("amount"));
        transfer.setTransferID(ts.getInt("transfer_id"));
        transfer.setTransferTypeId(ts.getInt("transfer_type_id"));
        transfer.setTransferStatusId(ts.getInt("transfer_status_id"));
        transfer.setUserFrom(ts.getString("user_from"));
        transfer.setUserTo(ts.getString("user_to"));
        transfer.setAccountTo(ts.getInt("account_to"));
        transfer.setAccountFrom(ts.getInt("account_from"));
        return transfer;



    }
}
