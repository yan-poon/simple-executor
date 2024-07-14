# Simple Executor Project

This project, named "simple-executor", is a Maven-based Java application that leverages the Spring Boot framework. It is demonstrating the effective use of Java's concurrency tools for improving the performance and responsiveness of web applications dealing with financial data, specifically coin prices. By leveraging the `ExecutorService` and its `invokeAny` method, the project showcases a non-blocking approach to executing multiple tasks concurrently, where the focus is on obtaining a single result from multiple potential sources as efficiently as possible.

This project aims to provide a practical example of how to implement asynchronous task execution in a Spring Boot application, making it a valuable resource for developers looking to enhance their applications with concurrent processing capabilities. The use of concurrency is particularly beneficial in scenarios where data can be fetched from multiple sources, and the application requires the fastest response without the need to wait for all tasks to complete.

Through the CoinPriceController, developers can learn how to structure their applications to handle concurrent data processing tasks, improving the overall throughput and user experience of their applications in environments where timely data processing and retrieval are critical.

## Features

- **Spring Boot Integration**: Built on the Spring Boot `2.6.1` version, ensuring compatibility with a wide range of Spring Boot features and third-party libraries.
- **Executor Service Usage**: Demonstrates how to use Java's ExecutorService for executing tasks asynchronously and concurrently, improving application performance.

## Getting Started

To get started with the Simple Executor Project, you will need Java and Maven installed on your system. Follow these steps:

1. **Clone the repository**:
   ```sh
   git clone https://github.com/yan-poon/simple-executor.git
   ```
2. **Navigate to the project directory**:
   ```sh
   cd simple-executor
   ```
3. **Build the project**:
   ```sh
   mvn clean install
   ```
4. **Run the application**:
   ```sh
   java -jar target/simple-executor-0.0.1-SNAPSHOT.jar
   ```

## Usage

This project is designed as a reference for using ExecutorService in Java applications. You can extend it by implementing your own concurrent tasks and integrating them into the application.

## Contributing

Contributions are welcome! Feel free to submit pull requests, report bugs, or suggest new features.

## License

This project is open-sourced under the MIT License. See the LICENSE file for more details.