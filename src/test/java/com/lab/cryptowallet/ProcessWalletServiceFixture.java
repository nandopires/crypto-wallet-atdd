package com.lab.cryptowallet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import com.lab.cryptowallet.service.WalletProcessService;
import com.lab.cryptowallet.vo.WalletAsset;

@RunWith(ConcordionRunner.class)
public class ProcessWalletServiceFixture {

	private Map<String, WalletAsset> walletAssets = new HashMap<>();

	public void addAsset(String assetId) {
		walletAssets.put(assetId, new WalletAsset(assetId));
	}

	public void addAsset(String assetId, String assetQuantity, String assetWalletPrice) {
		WalletAsset walletAsset = new WalletAsset(assetId);
		walletAsset.setQuantity(new BigDecimal(assetQuantity));
		walletAsset.setPrice(new BigDecimal(assetWalletPrice));
		walletAssets.put(assetId, walletAsset);
	}

	public void setPrice(String assetId, String assetCoincapPrice) {
		WalletAsset walletAsset = walletAssets.get(assetId);
		walletAsset.setCoinCapPrice(new BigDecimal(assetCoincapPrice));
	}

	public WalletAsset getAsset(String assetId) {
		return walletAssets.get(assetId);
	}

	public String calcPosition(String assetId) {
		WalletAsset walletAsset = walletAssets.get(assetId);
		return walletAsset.getPosition().toPlainString();
	}

	public String calcTotalWallet() {
		BigDecimal totalWallet = new BigDecimal(0);
		for (Entry<String, WalletAsset> walletAsset : walletAssets.entrySet()) {
			totalWallet = totalWallet.add(walletAsset.getValue().getPosition());
		}
		return totalWallet.toPlainString();
	}
	
	public String processWallet() {
		return new WalletProcessService().process();
	}
	
}
