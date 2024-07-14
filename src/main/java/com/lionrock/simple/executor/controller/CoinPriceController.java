package com.lionrock.simple.executor.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lionrock.simple.executor.model.CoinPrice;
import com.lionrock.simple.executor.service.CoinPriceService;
import com.lionrock.simple.executor.task.InsertCoinPriceCallableTask;
import com.lionrock.simple.executor.task.InsertCoinPriceRunnableTask;

@RestController
@RequestMapping("/coin-price")
public class CoinPriceController {

	private CoinPriceService coinPriceService;

	public CoinPriceController(CoinPriceService coinPriceService) {
		super();
		this.coinPriceService = coinPriceService;
	}

	private List<String> codes = List.of("USDC", "USDT", "BUSD", "PYUSD");

	@GetMapping("/latest/{code}")
	public CoinPrice getCoinPrice(@PathVariable("code") String code) {
		return coinPriceService.getLatestCoinPriceByCode(code);
	}

	@PostMapping("/latest/runnable")
	public String insertLatestCoinPriceRunnable() {
		System.out.println(String.format("%s Main Thread %s start the Runnable Task", LocalDateTime.now(),
				Thread.currentThread().getId()));
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		for (String code : codes) {
			executorService.execute(new InsertCoinPriceRunnableTask(code, this.coinPriceService));
		}
		executorService.shutdown();
		System.out.println(String.format("%s Main Thread %s completed the Runnable Task", LocalDateTime.now(),
				Thread.currentThread().getId()));
		return "Runnable Task Started";
	}

	@PostMapping("/latest/callable")
	public String insertLatestCoinPriceCallable() {
		System.out.println(String.format("%s Main Thread %s start the Callable Task", LocalDateTime.now(),
				Thread.currentThread().getId()));
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		for (String code : codes) {
			try {
				Future<CoinPrice> futurePrice = executorService
						.submit(new InsertCoinPriceCallableTask(code, this.coinPriceService));
				CoinPrice coinPrice = futurePrice.get();
				System.out.println(String.format("%s Main Thread %s got the coin price, coin: %s price:%s",
						coinPrice.getLocalDateTime(), Thread.currentThread().getId(), coinPrice.getCode(),
						coinPrice.getPrice()));
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		executorService.shutdown();
		System.out.println(String.format("%s Main Thread %s completed the Callable Task", LocalDateTime.now(),
				Thread.currentThread().getId()));
		return "Callable Task Started";
	}
}
