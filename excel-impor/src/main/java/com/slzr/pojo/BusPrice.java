package com.slzr.pojo;

import java.util.LinkedHashMap;
import java.util.Map;

public class BusPrice {
	private String pathNum;

	private Map<String, Integer> discount;

	private LinkedHashMap<String, Double> prices;

	public String getPathNum() {
		return pathNum;
	}

	public void setPathNum(String pathNum) {
		this.pathNum = pathNum;
	}

	public Map<String, Integer> getDiscount() {
		return discount;
	}

	public void setDiscount(Map<String, Integer> discount) {
		this.discount = discount;
	}

	public LinkedHashMap<String, Double> getPrices() {
		return prices;
	}

	public void setPrices(LinkedHashMap<String, Double> prices) {
		this.prices = prices;
	}

}
