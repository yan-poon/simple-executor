package com.lionrock.simple.executor.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lionrock.simple.executor.mapper.CoinPriceMapper;
import com.lionrock.simple.executor.model.CoinPrice;

@Service
public class CoinPriceService {

	private CoinPriceMapper coinPriceMapper;

	public CoinPriceService(CoinPriceMapper coinPriceMapper) {
		this.coinPriceMapper = coinPriceMapper;
	}

	public CoinPrice getLatestCoinPriceByCode(String code) {
		return coinPriceMapper.getLatestCoinPriceByCode(code.toUpperCase());
	}

	@Transactional
	public CoinPrice insertLatestCoinPrice(String code) throws InterruptedException {
		try {
			long threadId = Thread.currentThread().getId();
			System.out.println(
					String.format("%s Thread %s start insert price for %s", LocalDateTime.now(), threadId, code));
			Thread.sleep((long) (Math.random() * 2000));
			CoinPrice coinPrice = new CoinPrice();
			coinPrice.setCode(code.toUpperCase());
			coinPrice.setLocalDateTime(LocalDateTime.now());
			coinPrice.setPrice(generateRandomPrice());
			coinPrice.setUpdatedBy("Thread " + code.toUpperCase());
			coinPriceMapper.insertLatestCoinPrice(coinPrice);
			System.out.println(
					String.format("%s Thread %s completed insert price for %s", LocalDateTime.now(), threadId, code));
			return coinPrice;
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw e;
		}

	}

	private float generateRandomPrice() {
		if (Math.random() > 0.5) {
			return (float) (1 + (Math.random() / 1000));
		} else {
			return (float) (1 - (Math.random() / 1000));
		}
	}

}
