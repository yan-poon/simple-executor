package com.lionrock.simple.executor.task;

import com.lionrock.simple.executor.service.CoinPriceService;

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
public class InsertCoinPriceRunnableTask implements Runnable {

	private String code;
	private CoinPriceService coinPriceService;

	/**
	 * Constructs a new {@code InsertCoinPriceRunnableTask} with the specified coin code.
	 * <p>
	 * This constructor initializes a new instance of {@code InsertCoinPriceRunnableTask} with a given coin code.
	 * The coin code is used to identify the specific coin for which the latest price will be inserted into the
	 * database when the {@code run} method of this task is executed.
	 * </p>
	 * <p>
	 * This setup allows the {@code InsertCoinPriceRunnableTask} to perform its operation when executed by a thread
	 * or thread pool, making it suitable for asynchronous and concurrent execution in environments where coin price
	 * data needs to be updated frequently.
	 * </p>
	 *
	 * @param code The unique code identifying the coin for which the latest price is to be inserted.
	 */
	public InsertCoinPriceRunnableTask(String code, CoinPriceService coinPriceService) {
		this.coinPriceService = coinPriceService;
		this.code = code;
	}

	/**
	 * Executes the task of inserting the latest coin price into the database.
	 * <p>
	 * When invoked, this method performs the operation of inserting the latest price information for the coin
	 * identified by the code provided during the instantiation of the {@code InsertCoinPriceRunnableTask}. The
	 * insertion is carried out by calling the appropriate method of a {@link CoinPriceService} instance, which
	 * handles the database interaction.
	 * </p>
	 * <p>
	 * This method is designed to be executed by a thread or thread pool, allowing for asynchronous and concurrent
	 * updates to the coin price data. It ensures that the latest price data is accurately and efficiently inserted
	 * into the database, making it available for retrieval and analysis.
	 * </p>
	 * <p>
	 * Note: This method does not return a value. Any exceptions or errors encountered during the execution of the
	 * task should be handled within the method, ensuring that they do not adversely affect the thread or application
	 * in which the task is running.
	 * </p>
	 */
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
