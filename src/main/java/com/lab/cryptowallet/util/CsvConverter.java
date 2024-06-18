package com.lab.cryptowallet.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import com.lab.cryptowallet.vo.WalletAsset;

public class CsvConverter {

	public static List<WalletAsset> convertCsvToAssets() {
		List<WalletAsset> walletAssets = new LinkedList<>();

		InputStream csvStream = CsvConverter.class.getClassLoader().getResourceAsStream("wallet.csv");
		BufferedReader csvBufferedReader = new BufferedReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8));

		Stream<String> lines = csvBufferedReader.lines().skip(1);

		lines.forEach(line -> {
			List<String> lineArray = Arrays.asList(line.split(","));
			walletAssets.add(new WalletAsset(lineArray.get(0), new BigDecimal(lineArray.get(1)),
					new BigDecimal(lineArray.get(2))));
		});

		return walletAssets;
	}

}
