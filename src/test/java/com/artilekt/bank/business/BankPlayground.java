package com.artilekt.bank.business;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.artilekt.bank.business.Account;
import com.artilekt.bank.business.Bank;

public class BankPlayground {

	@Test
	public void printHighValueAccounts() {
		Bank bank = new Bank();
		bank.openAccount(100);
		bank.openAccount(15);
		bank.openAccount(140);
		bank.openAccount(175);
		bank.openAccount(45);
		
		bank.selectAccounts(Bank.UPPER_RANGE_BALANCE_SELECTOR.apply(new BigDecimal(100)))
			.forEach(System.out::println);
	}
	

	@Test
	public void printTxnFees() {
		Bank bank = new Bank();
		Account a1 = bank.openAccount(100);
		Account a2 = bank.openAccount(15);

		a1.withdraw(new BigDecimal(10));
		a1.withdraw(new BigDecimal(20));
		a2.deposit(new BigDecimal(100));
		a2.deposit(new BigDecimal(100));
		a2.withdraw(new BigDecimal(30));
				
		System.out.println(bank.getTxnFees());
	}	
}
