package com.lab.cryptowallet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.lab.cryptowallet.thread.AssetDaemon;
import com.lab.cryptowallet.util.CsvConverter;
import com.lab.cryptowallet.vo.WalletAsset;

import lombok.extern.java.Log;

@Log
public class WalletProcessService {

	private static final String OUTPUT_FORMAT = "total=%s,best_asset=%s,best_performance=%s,worst_asset=%s,worst_performance=%s";

	private static final int DECIMAL_DIGITS = 2;

	private static final int TEN_SECONDS = 10000;

	private ExecutorService threadPool = Executors.newFixedThreadPool(3);

	public String process() {
		List<WalletAsset> walletAssets = CsvConverter.convertCsvToAssets();
		List<WalletAsset> walletAssetsProcessed = new LinkedList<>();

		loadCoincapPrices(walletAssets, walletAssetsProcessed);
		BigDecimal total = sumTotalPositions(walletAssetsProcessed);
		sortWalletAssetsByPerformance(walletAssetsProcessed);

		WalletAsset worstAsset = walletAssetsProcessed.get(0);
		WalletAsset bestAsset = walletAssetsProcessed.get(walletAssets.size() - 1);

		return String.format(OUTPUT_FORMAT, total.setScale(DECIMAL_DIGITS, RoundingMode.HALF_UP).toPlainString(),
				bestAsset.getSymbol(), bestAsset.getPerformance(), worstAsset.getSymbol(), worstAsset.getPerformance());
	}

	private BigDecimal sumTotalPositions(List<WalletAsset> walletAssetsProcessed) {
		BigDecimal total = new BigDecimal(0);
		for (WalletAsset walletAsset : walletAssetsProcessed) {
			total = total.add(walletAsset.getPosition());
		}
		return total;
	}

	private void sortWalletAssetsByPerformance(List<WalletAsset> walletAssetsProcessed) {
		walletAssetsProcessed.sort(new Comparator<WalletAsset>() {
			public int compare(WalletAsset walletAsset1, WalletAsset walletAsset2) {
				return walletAsset1.getPerformance().compareTo(walletAsset2.getPerformance());
			};
		});
	}

	private void loadCoincapPrices(List<WalletAsset> walletAssets, List<WalletAsset> walletAssetsProcessed) {
		while (walletAssetsProcessed.size() < walletAssets.size()) {
			runThreadPool(walletAssets, walletAssetsProcessed);
		}
		threadPool.shutdown();
	}

	private void runThreadPool(List<WalletAsset> walletAssets, List<WalletAsset> walletAssetsProcessed) {
		List<Callable<Object>> assetsToProcess = new ArrayList<Callable<Object>>();
		for (WalletAsset walletAsset : walletAssets) {
			AssetDaemon assetDaemon = new AssetDaemon();
			assetDaemon.setWalletAsset(walletAsset);

			if (walletAsset.getCoinCapPrice() == null) {
				assetsToProcess.add(Executors.callable(assetDaemon));
			} else {
				walletAssetsProcessed.add(walletAsset);
			}
		}
		invokeAssetThreadGroup(assetsToProcess);
	}

	private void invokeAssetThreadGroup(List<Callable<Object>> assetsToProcess) {
		try {
			threadPool.invokeAll(assetsToProcess, Integer.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.log(Level.SEVERE, "Thread Interrupted");
		}
	}

	public static void main(String[] args) {
	}

	public static CoincapAssetService configureCoincapAssetService() {
		CoincapAssetService coincapAssetService = new CoincapAssetService();
		coincapAssetService.setEndpoint("http://api.coincap.io/v2/assets");
		coincapAssetService.setToken("eaa33432-5fff-4d6b-8e85-52e9897071e9");
		coincapAssetService.setQuery("search");
		coincapAssetService.setTimeoutMilliseconds(TEN_SECONDS);
		return coincapAssetService;
	}

}