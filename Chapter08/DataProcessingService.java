package com.example;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class DataProcessingService {

    public List<Result> processData(List<Data> dataList) {
        return dataList.parallelStream()
                .map(this::processDataItem)
                .collect(Collectors.toList());
    }

    private Result processDataItem(Data data) {
        // Placeholder logic for processing data
        Result result = new Result();
        result.setValue("Processed: " + data.getValue());
        return result;
    }
}
