package com.lionrock.simple.executor.task;

import com.lionrock.simple.executor.service.CoinPriceService;

public class InsertCoinPriceRunnableTask implements Runnable {

	private String code;
	private CoinPriceService coinPriceService;

	public InsertCoinPriceRunnableTask(String code, CoinPriceService coinPriceService) {
		this.coinPriceService = coinPriceService;
		this.code = code;
	}

	@Override
	public void run() {
		try {
			this.coinPriceService.insertLatestCoinPrice(this.code);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
