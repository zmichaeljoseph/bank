package com.artilekt.bank.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artilekt.bank.business.FeeCalculator;

public class Account implements Serializable {
	private BigDecimal balance = new BigDecimal("0.00");
	private String accountNumber;
	private long lastTxnTS = System.currentTimeMillis();
	private int txnCounter = 0;
	private BigDecimal txnFee = new BigDecimal("0.00");
	
	private AccountType accountType = AccountType.CHECKING;
	private TxnCallback txnCallback = TxnCallback.NOOP;
	private FeeCalculator feeCalculator = FeeCalculator.ZERO_FEE_CALCULATOR;
	
	private Client owner;
	
	private Logger logger = LoggerFactory.getLogger(Account.class);
	
	public Account() {}
	
	public Account(String accountNumber) {
		this(accountNumber, BigDecimal.ZERO, AccountType.CHECKING);
	}

	public Account(String accountNumber, BigDecimal balance) {
		this(accountNumber, balance, AccountType.CHECKING);
	}
	
	public Account(String accountNumber, BigDecimal balance, AccountType accountType) {
		this.balance = balance;
		this.accountType = accountType;
		this.accountNumber = accountNumber;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void deposit(BigDecimal amount) {
		logger.info("depositing ["+amount+"]");
		validateAmount(amount);
		balance = balance.add(amount);
		updateTxnTS();
		txnCallback.amountDeposited(this, amount);
		txnFee = txnFee.add(feeCalculator.calculateDepositFee(amount, this));
	}


	public void withdraw(BigDecimal amount) {
		logger.info("withdrawing ["+amount+"]");
		validateAmount(amount);
		if (balance.compareTo(amount) < 0)
			throw new IllegalArgumentException("Account overdraw: can't withdraw ["+amount+"] from account with balance ["+balance+"]");
		balance = balance.subtract(amount);
		updateTxnTS();
		txnCallback.amountWithdrawn(this, amount);
		txnFee = txnFee.add(feeCalculator.calculateWithdrawlFee(amount, this));
	}
	
	
	public void transferTo(Account account, BigDecimal amount) {
		transferFromTo(this, account, amount);
	}
	
	public void transferFrom(Account account, BigDecimal amount) {
		transferFromTo(account, this, amount);
	}
	
	private void transferFromTo(Account acc1, Account acc2, BigDecimal amount) {
		acc1.withdraw(amount);
		acc2.deposit(amount);
		updateTxnTS();
	}
	

	public BigDecimal getBalance() {
		return balance;
	}
	

	public String getAccountNumber() {
		return accountNumber;
	}
	
	public BigDecimal getTxnFee() { return txnFee; }
	
	public void setTxnCallback(TxnCallback txnCallback) {
		this.txnCallback = new ExcLoggingTCProxy(txnCallback);
	}
	
	@Override
	public String toString() {
		return "Account [number=" + accountNumber + ", balance=" + balance + "]";
	}
	
	//================	
	private void validateAmount(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException("Amount must be a positive number ["+amount+"]");
	}
		
	private void updateTxnTS() {
		lastTxnTS = System.currentTimeMillis();
		txnCounter++;
	}

	//===============
	private class ExcLoggingTCProxy implements TxnCallback {
		private TxnCallback txnCallback;
		
		private ExcLoggingTCProxy(TxnCallback txnCallback) {
			this.txnCallback = txnCallback;
		}

		@Override
		public void amountWithdrawn(Account account, BigDecimal amount) {
			try { txnCallback.amountWithdrawn(account, amount); }
			catch (Throwable th) {
				logger.error("Unexpected exception in TxnCallback call", th);
			}
		}

		@Override
		public void amountDeposited(Account account, BigDecimal amount) {
			try { txnCallback.amountDeposited(account, amount); }
			catch (Throwable th) {
				logger.error("Unexpected exception in TxnCallback call", th);
			}
		}
		
	}

	public void setFeeCalculator(FeeCalculator feeCalculator) {
		this.feeCalculator = feeCalculator;
	}

	public void setOwner(Client owner) { this.owner = owner; }
	public Client getOwner() { return owner; }

}
