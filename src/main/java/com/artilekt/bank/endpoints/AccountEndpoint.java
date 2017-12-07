package com.artilekt.bank.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.artilekt.bank.business.Account;
import com.artilekt.bank.business.Bank;
import com.artilekt.bank.business.CRM;
import com.artilekt.bank.dto.AccountCreationRequest;

@RestController
@RequestMapping("/accounts")
public class AccountEndpoint {
	@Autowired
	private Bank bank;
	
	@Autowired
	private CRM crm;
	
	@PostMapping
	public Account openAccount(@RequestBody AccountCreationRequest req) {
		Account acc = bank.openAccount(req.getInitialBalance(), req.getAccountType());
		// here is how we can potentially assign Customer to Account
//		acc.setOwner(crm.findCustomer(req.getOwnerDriverLicense()));
		return acc;
	}
	
}
