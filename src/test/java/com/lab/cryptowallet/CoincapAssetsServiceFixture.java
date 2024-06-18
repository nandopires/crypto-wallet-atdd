package com.lab.cryptowallet;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import com.lab.cryptowallet.service.CoincapAssetService;
import com.lab.cryptowallet.vo.WalletAsset;

import lombok.Data;

@RunWith(ConcordionRunner.class)
@Data
public class CoincapAssetsServiceFixture {
	
	private CoincapAssetService coincapAssetService = new CoincapAssetService();
	
	public void configure(String endpoint, String token, String query, String assetSymbol) {
		this.coincapAssetService.setEndpoint(endpoint);
		this.coincapAssetService.setToken(token);
		this.coincapAssetService.setQuery(query);
		this.coincapAssetService.setAssetSymbol(assetSymbol);
	}
	
	public WalletAsset getAsset() {
		return coincapAssetService.getAsset();
	}

}