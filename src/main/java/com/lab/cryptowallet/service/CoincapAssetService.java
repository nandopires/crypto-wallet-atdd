package com.lab.cryptowallet.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lab.cryptowallet.vo.WalletAsset;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.Data;

@Data
public class CoincapAssetService {
	private static final int ONE_SECOND = 1000;
	
	private String endpoint;
	private String token;
	private String query;
	private String assetSymbol;
	private Integer timeoutMilliseconds = ONE_SECOND;
	
	public WalletAsset getAsset() throws UnirestException {
		HttpResponse<String> responseAssets = Unirest.get(this.getEndpoint())
				.header("accept", "*/*")
				.header("content-type", "application/json; charset=utf-8")
				.header("accept-encoding", "gzip, deflate")
				.header("Authorization", "Bearer "+token)
				.queryString(query, assetSymbol)
				.socketTimeout(timeoutMilliseconds)
				.asString();
		
		JsonObject responseJson = new Gson().fromJson(responseAssets.getBody(), JsonObject.class);

		JsonObject coincapAsset = responseJson.get("data").getAsJsonArray().get(0).getAsJsonObject();
		
		WalletAsset walletAsset = new WalletAsset(coincapAsset.get("id").getAsString());
		walletAsset.setCoinCapPrice(coincapAsset.get("priceUsd").getAsBigDecimal());
		
		return walletAsset;
	}
	
}