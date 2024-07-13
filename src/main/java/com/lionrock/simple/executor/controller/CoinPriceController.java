package com.lionrock.simple.executor.controller;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lionrock.simple.executor.model.CoinPrice;
import com.lionrock.simple.executor.service.CoinPriceService;
import com.lionrock.simple.executor.task.InsertCoinPriceTask;

@RestController
@RequestMapping("/coin-price")
public class CoinPriceController {

	private CoinPriceService coinPriceService;

	public CoinPriceController(CoinPriceService coinPriceService) {
		super();
		this.coinPriceService = coinPriceService;
	}

	@GetMapping("/latest/{code}")
	public CoinPrice getCoinPrice(@PathVariable("code") String code) {
		return coinPriceService.getLatestCoinPriceByCode(code);
	}

	@PostMapping("/latest")
	public String insertLatestCoinPrice() {
		System.out.println(LocalDateTime.now() + " Main Thread Start update Coin Price");
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.execute(new InsertCoinPriceTask("USDC", this.coinPriceService));
		executorService.execute(new InsertCoinPriceTask("USDT", this.coinPriceService));
		executorService.execute(new InsertCoinPriceTask("BUSD", this.coinPriceService));
		executorService.execute(new InsertCoinPriceTask("PYUSD", this.coinPriceService));
		executorService.shutdown();
		System.out.println(LocalDateTime.now() + " Main Thread completed the task");
		return "Update Task Started";
	}
}
