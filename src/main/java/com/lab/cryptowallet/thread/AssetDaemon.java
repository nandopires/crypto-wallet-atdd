package com.lab.cryptowallet.thread;

import java.net.SocketTimeoutException;

import com.lab.cryptowallet.service.CoincapAssetService;
import com.lab.cryptowallet.service.WalletProcessService;
import com.lab.cryptowallet.vo.WalletAsset;

import kong.unirest.UnirestException;
import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class AssetDaemon implements Runnable {
	
	private WalletAsset walletAsset;
	
	@Override
	public void run() {
		try {
			CoincapAssetService coincapAssetService = WalletProcessService.configureCoincapAssetService();
			coincapAssetService.setAssetSymbol(walletAsset.getSymbol());
			WalletAsset asset = coincapAssetService.getAsset();
			walletAsset.setCoinCapPrice(asset.getCoinCapPrice());
			walletAsset.setId(asset.getId());
		} catch (UnirestException e) {
			if (e.getCause() instanceof SocketTimeoutException) {
				log.info("10 second timeout, asset will be reprocessed");
			}
		}
	}
}
