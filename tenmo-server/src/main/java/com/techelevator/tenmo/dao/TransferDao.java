package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public List<Transfer> getAllTransfers();

    public List<Transfer> getPendingTransfer();

    public Transfer getTransferByID(int userId);

    public String updatePendingRequest(int userId, Transfer transfer);

    public String sendMoney(int userFrom, int userTo, BigDecimal amount);

    public String requestMoney(int userFrom, int userTo, BigDecimal amount);
}
