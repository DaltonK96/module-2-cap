package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalance(int userId);
    BigDecimal addTEBucks (BigDecimal addAmount, int userId);
    BigDecimal subtractTEBucks (BigDecimal subtractAmount, int userId);
    Account getUserById (int userId);
    Account findAccountById(int id);


}
