package com.artilekt.bank.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CRM {
	private List<Client> clients = new ArrayList<>();

	public void addClient(Client client) {
		clients.add(client);
		
	}

	public Client findClient(AccountType ownerDriverLicense) {
		throw new UnsupportedOperationException("not yet implemented");
	}

}
