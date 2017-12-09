package com.artilekt.bank.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AM {
	private List<Asset> assets = new ArrayList<>();

	public void addAsset(Asset asset) {
		if (contains(asset))
			throw new ClientDuplicateException("Asset ["+asset+"] already exists");
		assets.add(asset);
	}

	public Asset findAssetByDesc(String desc) {
		return findAssetByDesc(desc);
	}
	

	public List<Asset> searchAssets(Predicate<Asset> searchCriteria) {
		return assets.stream()
					.filter(searchCriteria::test)
					.collect(Collectors.toList());
	}

	public List<Asset> listAllAssets() {
		return Collections.unmodifiableList(assets);
	}

	
	public boolean contains(Asset asset) {
		return assets.contains(asset);
	}


}
