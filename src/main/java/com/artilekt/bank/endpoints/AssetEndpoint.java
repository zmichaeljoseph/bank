package com.artilekt.bank.endpoints;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.artilekt.bank.business.AM;
import com.artilekt.bank.business.Asset;
import com.artilekt.bank.business.CRM;
import com.artilekt.bank.business.Client;

@RestController
@RequestMapping("/assets")
public class AssetEndpoint<am> {
	
	@Autowired
	private AM am;
	
	@PostMapping
	public Asset createAsset(@RequestBody Asset asset) {
		am.addAsset(asset);
		return asset;
	}
	
	@PutMapping("LEARNING IN PROGRESS")
	public Double setAssetValue(@RequestBody Asset asset, Double value) {
		asset.setValue(value) ;
		return asset.getValue();
		
	}
	
	@GetMapping
	public List<Asset> assetClients() {
		return am.listAllAssets();
	}
	

}
