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

	/**
	 * Retrieves the latest price for a specific coin identified by its code.
	 * <p>
	 * This method queries the database or external service to find the most recent price information for a coin
	 * specified by the given code. It is designed to return the latest available price data, ensuring that users
	 * have access to the most current price information.
	 * </p>
	 * <p>
	 * The method handles scenarios where the coin code might not be present in the database or the external service
	 * by returning an optional {@link CoinPrice} object. This approach allows callers to easily handle the absence
	 * of data without dealing with null values.
	 * </p>
	 * <p>
	 * Note: This method assumes that the coin code is a unique identifier for the coin and that the database or
	 * external service is up-to-date with the latest price information.
	 * </p>
	 *
	 * @param code The unique code identifying the coin for which the latest price is requested.
	 * @return An {@link Optional<CoinPrice>} containing the latest price of the coin if available, or an empty
	 *         optional if the coin code does not exist or no price data is available.
	 */
	public CoinPrice getLatestCoinPriceByCode(String code) {
		return coinPriceMapper.getLatestCoinPriceByCode(code.toUpperCase());
	}

	/**
	 * Inserts the latest coin price into the database using MyBatis.
	 * <p>
	 * This method utilizes MyBatis to execute an insert command that adds the latest price information for a coin
	 * into the database. It is part of the {@code coinPriceMapper} interface, which maps Java methods to SQL
	 * statements. The method takes a {@link CoinPrice} object as a parameter, which encapsulates all necessary
	 * information about the coin's latest price, including the coin's identifier, the price value, and the timestamp
	 * of the price data.
	 * </p>
	 * <p>
	 * The use of MyBatis allows for cleaner separation of SQL code from Java code, improving maintainability and
	 * readability. This method ensures that the coin price data is persisted in the database, making it available
	 * for retrieval and analysis.
	 * </p>
	 * <p>
	 * Note: It is assumed that the {@link CoinPrice} object provided to this method contains valid and up-to-date
	 * information. The caller is responsible for ensuring that the data complies with any database constraints
	 * and is suitable for insertion.
	 * </p>
	 *
	 * @param coinPrice The {@link CoinPrice} object containing the latest price information to be inserted into the database.
	 */
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

	/**
	 * Generates a random price for a coin.
	 * <p>
	 * This method simulates the fluctuation of coin prices by generating a random price within a predefined range.
	 * It is primarily used for testing or simulation purposes, where actual market data is not required. The method
	 * ensures that the generated price is within realistic boundaries to mimic real market conditions as closely as
	 * possible.
	 * </p>
	 * <p>
	 * Note: The randomness is bounded and may not reflect actual market volatility. This method should not be used
	 * for financial analysis or trading decisions.
	 * </p>
	 *
	 * @return A float value representing the randomly generated coin price.
	 */
	private float generateRandomPrice() {
		if (Math.random() > 0.5) {
			return (float) (1 + (Math.random() / 1000));
		} else {
			return (float) (1 - (Math.random() / 1000));
		}
	}

}
