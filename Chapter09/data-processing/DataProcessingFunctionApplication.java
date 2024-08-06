package com.example;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DataProcessingFunctionApplication {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessingFunctionApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DataProcessingFunctionApplication.class, args);
    }

    @Bean
    public Function<List<Data>, List<ProcessedData>> dataProcessing() {
        return dataList -> {
            logger.info("Starting data processing");

            List<ProcessedData> processedDataList = dataList.parallelStream()
                    .map(this::processDataItem)
                    .collect(Collectors.toList());

            logger.info("Data processing completed");
            return processedDataList;
        };
    }

    private ProcessedData processDataItem(Data data) {
        // Complex data processing logic
        return new ProcessedData(data);
    }
}
