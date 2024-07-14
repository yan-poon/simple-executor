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

/**
 * Controller class for managing coin prices.
 * <p>
 * This class provides endpoints for inserting and retrieving the latest coin prices. It utilizes an
 * {@link ExecutorService} to execute tasks asynchronously, allowing for the retrieval and insertion of coin
 * prices in a non-blocking manner. The class demonstrates the use of asynchronous programming patterns
 * within a Spring Boot application context.
 * </p>
 */
@RestController
@RequestMapping("/coin-price")
public class CoinPriceController {

	private CoinPriceService coinPriceService;

	public CoinPriceController(CoinPriceService coinPriceService) {
		super();
		this.coinPriceService = coinPriceService;
	}

	private List<String> codes = List.of("USDC", "USDT", "BUSD", "PYUSD");

	/**
	 * Retrieves the latest price for a specific coin.
	 * <p>
	 * This method handles HTTP GET requests to fetch the latest price of a coin identified by its code.
	 * It utilizes the {@link CoinPriceService} to query the database for the latest price information.
	 * </p>
	 *
	 * @param code The code of the coin for which the latest price is requested.
	 * @return A response entity containing the latest {@link CoinPrice} for the specified coin, or an error message if not found.
	 */
	@GetMapping("/latest/{code}")
	public CoinPrice getCoinPrice(@PathVariable("code") String code) {
		return coinPriceService.getLatestCoinPriceByCode(code);
	}
	
	/**
	 * Initiates an asynchronous task to insert the latest coin prices using a runnable task.
	 * <p>
	 * This method leverages a {@link ExecutorService} to execute {@link InsertCoinPriceRunnableTask} instances
	 * concurrently. Each task is responsible for fetching and inserting the latest price of a specific coin
	 * into the database. The method demonstrates an approach to handle asynchronous operations without
	 * expecting a return value from the tasks.
	 * </p>
	 * <p>
	 * The use of a runnable task is suitable for operations where the outcome does not need to be immediately
	 * returned to the caller. This method ensures that the coin price update operations do not block the main
	 * execution thread, allowing the system to handle other requests concurrently.
	 * </p>
	 * <p>
	 * Note: This method is designed for demonstration purposes and may require adjustments for error handling
	 * and scalability in a production environment.
	 * </p>
	 */
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

	/**
	 * Initiates an asynchronous task to insert the latest coin prices using a callable task.
	 * <p>
	 * This method utilizes an {@link ExecutorService} to execute {@link InsertCoinPriceCallableTask} instances
	 * concurrently. Unlike runnable tasks, each callable task is responsible for fetching and inserting the latest
	 * price of a specific coin and returns a {@link Future<CoinPrice>} representing the pending result of the task.
	 * This allows for the retrieval of the operation's outcome, enabling further processing or handling based on
	 * the task's result.
	 * </p>
	 * <p>
	 * The callable approach is particularly useful for operations where the outcome needs to be known post-execution,
	 * such as logging the result or updating the UI based on the success or failure of the operation. This method
	 * demonstrates handling asynchronous operations with the ability to process the results once available.
	 * </p>
	 * <p>
	 * Note: This method is designed for demonstration purposes and may require adjustments for comprehensive error
	 * handling, result processing, and scalability in a production environment.
	 * </p>
	 */
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

	/**
	 * Executes multiple callable tasks to insert the latest coin prices simultaneously and waits for all to complete.
	 * <p>
	 * This method employs the {@link ExecutorService#invokeAll(Collection)} method to execute a collection of
	 * {@link InsertCoinPriceCallableTask} instances concurrently. Each task is responsible for fetching and inserting
	 * the latest price of a specific coin. The invokeAll method blocks until all tasks have completed execution,
	 * either normally or via cancellation.
	 * </p>
	 * <p>
	 * Utilizing invokeAll simplifies the process of managing a group of callable tasks, especially when the outcome
	 * of all tasks is required before proceeding. This approach is beneficial for batch processing scenarios where
	 * the completion of all tasks is necessary to ensure data consistency or to perform subsequent operations.
	 * </p>
	 * <p>
	 * Note: This method showcases a synchronous blocking approach to handling multiple asynchronous tasks and may
	 * need to be adapted for use cases requiring non-blocking behavior or more sophisticated error handling and
	 * result processing strategies in a production environment.
	 * </p>
	 */
	@PostMapping("/latest/invokeAll")
	public String insertLatestCoinPriceInvokeAll() throws InterruptedException, ExecutionException {
		System.out.println(String.format("%s Main Thread %s start the InvokeAll Task", LocalDateTime.now(),
				Thread.currentThread().getId()));
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		List<InsertCoinPriceCallableTask> tasks = new ArrayList<>();
		for (String code : codes) {
			tasks.add(new InsertCoinPriceCallableTask(code, this.coinPriceService));
		}

		List<Future<CoinPrice>> futurePrices = executorService.invokeAll(tasks);
		for (Future<CoinPrice> futurePrice : futurePrices) {
			CoinPrice coinPrice = futurePrice.get();
			System.out.println(String.format("%s Main Thread %s got the coin price, coin: %s price:%s",
					coinPrice.getLocalDateTime(), Thread.currentThread().getId(), coinPrice.getCode(),
					coinPrice.getPrice()));
		}
		executorService.shutdown();
		System.out.println(String.format("%s Main Thread %s completed the InvokeAll Task", LocalDateTime.now(),
				Thread.currentThread().getId()));
		return "InvokeAll Task Started";
	}
	
	/**
	 * Executes a collection of callable tasks to insert the latest coin prices and returns the result of one of the successfully executed tasks.
	 * <p>
	 * This method makes use of the {@link ExecutorService#invokeAny(Collection)} method to execute a collection of
	 * {@link InsertCoinPriceCallableTask} instances concurrently. Unlike {@link ExecutorService#invokeAll(Collection)},
	 * invokeAny does not wait for all tasks to complete. Instead, it returns the result of one of the callable tasks
	 * that completes execution successfully (i.e., without throwing an exception), effectively ignoring the results or
	 * failures of other tasks. This method is particularly useful when the same result can be obtained from multiple
	 * tasks, and there is no need to wait for all tasks to complete.
	 * </p>
	 * <p>
	 * The use of invokeAny simplifies the execution of multiple tasks when only a single result is needed, reducing
	 * the waiting time when compared to waiting for all tasks to complete. This approach is beneficial in scenarios
	 * where multiple sources can provide the same information, and the fastest response is preferred.
	 * </p>
	 * <p>
	 * Note: This method demonstrates a non-blocking approach to handling multiple asynchronous tasks but may need
	 * adjustments for comprehensive error handling, especially in scenarios where it's crucial to ensure that at least
	 * one task succeeds in a production environment.
	 * </p>
	 */
	@PostMapping("/latest/invokeAny")
	public String insertLatestCoinPriceInvokeAny() throws InterruptedException, ExecutionException {
		System.out.println(String.format("%s Main Thread %s start the InvokeAny Task", LocalDateTime.now(),
				Thread.currentThread().getId()));
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		List<InsertCoinPriceCallableTask> tasks = new ArrayList<>();
		for (String code : codes) {
			tasks.add(new InsertCoinPriceCallableTask(code, this.coinPriceService));
		}
		CoinPrice coinPrice = executorService.invokeAny(tasks);		
		System.out.println(String.format("%s Main Thread %s got the coin price, coin: %s price:%s",
				coinPrice.getLocalDateTime(), Thread.currentThread().getId(), coinPrice.getCode(),
				coinPrice.getPrice()));
		executorService.shutdown();
		System.out.println(String.format("%s Main Thread %s completed the InvokeAny Task", LocalDateTime.now(),
				Thread.currentThread().getId()));
		return "InvokeAny Task Started";
	}
}
