package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public List<Transfer> getAllTransfers(int userId);

    public List<Transfer> getPendingTransfer(int userId);

    public Transfer getTransferByID(int transferId);

    public String sendMoney(int userFrom, int userTo, BigDecimal amount);

    public String requestMoney(int userFrom, int userTo, BigDecimal amount);

    public String updatePendingRequest(int statusId, Transfer transfer);
}
