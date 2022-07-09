package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {


    private AccountDao accountDao;
    private UserDao userDao;


    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    //TODO: Make sure that only the user who is logged in can check their own balance
    //Check balance of account
    @RequestMapping(value = "account/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal user) {
        String username = user.getName();
        BigDecimal balance = accountDao.getBalance(userDao.findIdByUsername(username));
        return balance;
    }

    //TODO: Allow only the admin to see all users
    //Check balance of all accounts
    //@PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "account/all", method = RequestMethod.GET)
    public List<User> listOfUsers() {
        List<User> accounts = userDao.findAll();
        return accounts;
    }

}
