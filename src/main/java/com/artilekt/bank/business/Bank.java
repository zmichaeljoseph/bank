package com.artilekt.bank.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class Bank {
	private Logger logger = LoggerFactory.getLogger(Bank.class);
	private List<Account> accounts = new ArrayList<>();
	private AccountNumberGenerator accountNumberGenerator = AccountNumberGenerator.DEFAULT;
	
	public final static Function<BigDecimal, Predicate<Account>> UPPER_RANGE_BALANCE_SELECTOR = amount -> account -> account.getBalance().compareTo(amount) > 0;
	public final static Function<BigDecimal, Predicate<Account>> LOWER_RANGE_BALANCE_SELECTOR = amount -> account -> account.getBalance().compareTo(amount) < 0;
	public final static BiFunction<BigDecimal, BigDecimal, Predicate<Account>> MID_RANGE_BALANCE_SELECTOR = 
			(fromAmount, toAmount) -> UPPER_RANGE_BALANCE_SELECTOR.apply(fromAmount).and(LOWER_RANGE_BALANCE_SELECTOR.apply(toAmount));
	public final static Function<String, Predicate<Account>> ACCOUNT_NUMBER_SELECTOR = accountNumber -> account -> account.getAccountNumber().equals(accountNumber);
	public final static Function<String, Predicate<Account>> CLIENT_ID_SELECTOR = clientId -> account -> account.getOwner().getClientId().equals(clientId);
			
	// Add curried functions for selector by activity (active accounts for the last [timeframe])
	public final static BiFunction<BigDecimal, Account, BigDecimal> DEFAULT_WITHDRAWL_FEE = (amount, account) -> amount.multiply(new BigDecimal("0.01"));
	public final static BiFunction<BigDecimal, Account, BigDecimal> DEFAULT_DEPOSIT_FEE   = FeeCalculator.ZERO_FEE;

	
	private FeeCalculator.Manager feeCalculator = FeeCalculator.create(DEFAULT_WITHDRAWL_FEE, DEFAULT_DEPOSIT_FEE);

			
	public Account openAccount() {
		return openAccount(BigDecimal.ZERO, AccountType.CHECKING);
	}
		
	public Account openAccount(int initialBalance) {
		return openAccount(new BigDecimal(initialBalance), AccountType.CHECKING);
	}
		
	public Account openAccount(BigDecimal initialBalance) {
		return openAccount(initialBalance, AccountType.CHECKING);
	}
		
	public Account openAccount(BigDecimal initialBalance, AccountType accountType) {
		Account acc = new Account(accountNumberGenerator.generateAccountNumber(), initialBalance, accountType);
		acc.setFeeCalculator(feeCalculator);
		accounts.add(acc); 
		return acc;
	}
	
	public BigDecimal getTxnFees() { 
		return accounts.stream().map(Account::getTxnFee).reduce(new BigDecimal("0.00"), (a, b) -> a.add(b));
	}
	
	public List<Account> listAllAccounts() {
		return Collections.unmodifiableList(accounts);
	}

	public List<Account> selectAccounts(Predicate<Account> condition) {
		return accounts.stream().filter(condition).collect(Collectors.toList());
	}
	
	public List<Account> findAccountsByClientId(String clientId) { return selectAccounts(CLIENT_ID_SELECTOR.apply(clientId)); }
	
	public Account findAccountByNumber(String accountNumber) {
		Optional<Account> acc = accounts.stream().filter(ACCOUNT_NUMBER_SELECTOR.apply(accountNumber)).findFirst();
		if (acc.isPresent())   return acc.get();
			
		throw new AccountNotFoundException("Account ["+accountNumber+"] not found");
	}
	
	public void setWithdrawlFeeCalcAlgo(BiFunction<BigDecimal, Account, BigDecimal> withdrawlFeeCalcAlgo) { feeCalculator.setWithdrawlFeeCalcAlgo(withdrawlFeeCalcAlgo);	}
	public void setDepositlFeeCalcAlgo (BiFunction<BigDecimal, Account, BigDecimal> depositFeeCalcAlgo)   { feeCalculator.setDepositFeeCalcAlgo(depositFeeCalcAlgo);	}
	
	
}
