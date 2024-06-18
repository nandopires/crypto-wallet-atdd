package com.lab.cryptowallet.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Data;

@Data
public class WalletAsset {

	private String id;
	private String symbol;
	private BigDecimal price;
	private BigDecimal coinCapPrice;
	private BigDecimal quantity;

	public WalletAsset(String id) {
		this.id = id;
	}
	
	public WalletAsset(String symbol, BigDecimal quantity, BigDecimal price) {
		this.symbol = symbol;
		this.quantity = quantity;
		this.price = price;
	}

	public BigDecimal getPosition() {
		return quantity.multiply(coinCapPrice);
	}

	public BigDecimal getPerformance() {
		return coinCapPrice.divide(price, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
	}

}
