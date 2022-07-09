package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    //Get all transfers for one user
    @RequestMapping(value = "account/{id}/transfer", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(@PathVariable("id") int userId) {
        List<Transfer> transfers = transferDao.getAllTransfers(userId);
        return transfers;
    }

    //Get specific transfer via Id
    @RequestMapping(value = "account/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferId(@PathVariable int transferId) {
        Transfer transfer = transferDao.getTransferByID(transferId);
        return transfer;
    }

    //Send transfer
    //send post(principal account from) who to send and how much (request body/param)
    @RequestMapping(value = "account/transfer", method = RequestMethod.POST)
    public Account sendTransfer(@RequestBody TransferDTO transfer) {
        transferDao.sendMoney(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        Account account = accountDao.findAccountById(transfer.getAccountFrom());
        return account;
    }


    //Request transfer


    //See transfer request


    //update transfer
    //RequestMethod.PUT

}
