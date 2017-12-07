package com.artilekt.bank.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.artilekt.bank.business.Account;
import com.artilekt.bank.business.Bank;
import com.artilekt.bank.business.CRM;
import com.artilekt.bank.business.Client;
import com.artilekt.bank.dto.AccountCreationRequest;

@RestController
@RequestMapping("/clients")
public class ClientEndpoint {
	@Autowired
	private CRM crm;
	
	@PostMapping
	public Client createClient(@RequestBody Client client) {
		crm.addClient(client);
		return client;
	}
	
}
