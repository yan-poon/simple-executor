package com.lionrock.simple.executor.task;

import java.util.concurrent.Callable;

import com.lionrock.simple.executor.model.CoinPrice;
import com.lionrock.simple.executor.service.CoinPriceService;

/**
 * Represents a callable task for inserting the latest coin price into the database.
 * <p>
 * This class is designed to be executed asynchronously, allowing for the concurrent insertion of coin prices
 * into the database. It implements the {@link Callable} interface, enabling it to be submitted to an
 * {@link ExecutorService} for execution. The task uses a {@link CoinPriceService} to perform the actual
 * insertion of the coin price into the database.
 * </p>
 * <p>
 * Upon execution, the task calls the {@code insertLatestCoinPrice} method of the {@link CoinPriceService},
 * passing the coin's code. It then returns the {@link CoinPrice} object that was inserted into the database,
 * allowing for the retrieval of the inserted coin price data.
 * </p>
 * <p>
 * This approach to inserting coin prices is beneficial in scenarios requiring the update of coin price
 * information in a concurrent manner, thereby improving the efficiency and responsiveness of the system.
 * </p>
 *
 * @see Callable
 * @see ExecutorService
 * @see CoinPriceService
 */
public class InsertCoinPriceCallableTask implements Callable<CoinPrice> {

	private String code;
	private CoinPriceService coinPriceService;

	/**
	 * Constructs a new {@code InsertCoinPriceCallableTask} with the specified coin code and coin price service.
	 * <p>
	 * This constructor initializes a new instance of {@code InsertCoinPriceCallableTask} with a given coin code
	 * and a reference to a {@link CoinPriceService}. The coin code is used to identify the specific coin for which
	 * the latest price will be inserted into the database. The {@link CoinPriceService} provides the functionality
	 * to insert the coin price into the database.
	 * </p>
	 * <p>
	 * This setup allows the {@code InsertCoinPriceCallableTask} to perform its operation independently when executed
	 * by an {@link ExecutorService}, making it suitable for asynchronous execution in a concurrent environment.
	 * </p>
	 *
	 * @param code The unique code identifying the coin for which the latest price is to be inserted.
	 * @param coinPriceService The service responsible for inserting the coin price into the database.
	 */
	public InsertCoinPriceCallableTask(String code, CoinPriceService coinPriceService) {
		this.code = code;
		this.coinPriceService = coinPriceService;
	}

	/**
	 * Executes the task of inserting the latest coin price into the database.
	 * <p>
	 * This method is called when the {@code InsertCoinPriceCallableTask} is executed by an {@link ExecutorService}.
	 * It invokes the {@code insertLatestCoinPrice} method of the {@link CoinPriceService}, passing the coin's code
	 * to insert the latest price for that specific coin into the database.
	 * </p>
	 * <p>
	 * Upon successful insertion, it returns the {@link CoinPrice} object that represents the newly inserted coin
	 * price data. This allows for the inserted data to be further processed or verified by the caller.
	 * </p>
	 * <p>
	 * Note: This method may throw an exception if the insertion operation fails. The exception should be handled
	 * by the caller to ensure proper error management and logging.
	 * </p>
	 *
	 * @return The {@link CoinPrice} object representing the inserted coin price data.
	 * @throws Exception if there is an issue with inserting the coin price into the database.
	 */
	@Override
	public CoinPrice call() throws Exception {
		return this.coinPriceService.insertLatestCoinPrice(this.code);
	}

}
