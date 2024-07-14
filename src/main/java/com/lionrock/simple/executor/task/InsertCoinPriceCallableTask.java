package com.lionrock.simple.executor.task;

import java.util.concurrent.Callable;

import com.lionrock.simple.executor.model.CoinPrice;
import com.lionrock.simple.executor.service.CoinPriceService;

public class InsertCoinPriceCallableTask implements Callable<CoinPrice> {

	private String code;
	private CoinPriceService coinPriceService;

	public InsertCoinPriceCallableTask(String code, CoinPriceService coinPriceService) {
		this.code = code;
		this.coinPriceService = coinPriceService;
	}

	@Override
	public CoinPrice call() throws Exception {
		CoinPrice coinPrice=this.coinPriceService.insertLatestCoinPrice(this.code);
		return coinPrice;
	}

}
