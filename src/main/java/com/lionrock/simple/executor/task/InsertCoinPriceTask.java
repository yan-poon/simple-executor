package com.lionrock.simple.executor.task;

import com.lionrock.simple.executor.service.CoinPriceService;

public class InsertCoinPriceTask implements Runnable {

	private String code;
	private CoinPriceService coinPriceService;

	public InsertCoinPriceTask(String code, CoinPriceService coinPriceService) {
		this.coinPriceService = coinPriceService;
		this.code = code;
	}

	@Override
	public void run() {
		coinPriceService.insertLatestCoinPrice(this.code);
	}

}
