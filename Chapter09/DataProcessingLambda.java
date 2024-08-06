package com.example;

import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class DataProcessingLambda implements RequestHandler<List<Data>, List<ProcessedData>> {
    @Override
    public List<ProcessedData> handleRequest(List<Data> dataList, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Starting data processing");

        List<ProcessedData> processedDataList = dataList.parallelStream()
                .map(this::processDataItem)
                .collect(Collectors.toList());

        logger.log("Data processing completed");
        return processedDataList;
    }

    private ProcessedData processDataItem(Data data) {
        // Complex data processing logic
        return new ProcessedData(data);
    }
}
