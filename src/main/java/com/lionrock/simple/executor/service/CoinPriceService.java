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
	public int insertLatestCoinPrice(String code) {
		try {
			System.out.println(LocalDateTime.now() + " Start insert price for " + code);
			Thread.sleep((long) (Math.random() * 2000));
			CoinPrice coinPrice = new CoinPrice();
			coinPrice.setCode(code.toUpperCase());
			coinPrice.setLocalDateTime(LocalDateTime.now());
			coinPrice.setPrice(generateRandomPrice());
			coinPrice.setUpdatedBy("Thread " + code.toUpperCase());
			int affectedRow = coinPriceMapper.insertLatestCoinPrice(coinPrice);
			System.out.println(LocalDateTime.now() + " Completed insert price for " + code);
			return affectedRow;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
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
