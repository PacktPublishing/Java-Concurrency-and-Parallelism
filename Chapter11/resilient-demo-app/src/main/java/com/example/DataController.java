package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {

    private final DataReplicationService dataService;

    public DataController(DataReplicationService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/s3")
    public String replicateToS3(@RequestParam String key, @RequestParam String content) {
        dataService.replicateToS3(key, content, content);
        return "Data replicated to S3";
    }

    @PostMapping("/dynamo")
    public String replicateToDynamoDB(@RequestParam String key, @RequestParam String value) {
        dataService.replicateToDynamoDB(key, value);
        return "Data replicated to DynamoDB";
    }

    @GetMapping("/dynamo/{key}")
    public String retrieveFromDynamoDB(@PathVariable String key) {
        return dataService.retrieveFromDynamoDB(key).orElse("No data found");
    }

    @PostMapping("/dynamo/conflict")
    public String resolveConflict(@RequestParam String key, @RequestParam String newValue) {
        dataService.resolveConflict(key, newValue);
        return "Conflict resolved in DynamoDB";
    }

    @PostMapping("/backup/{key}")
    public String backupToS3(@PathVariable String key) {
        dataService.backupDynamoDBToS3(key);
        return "Data backed up to S3";
    }

    @PostMapping("/restore/{key}")
    public String restoreFromS3(@PathVariable String key) {
        dataService.restoreFromS3(key);
        return "Data restored from S3 to DynamoDB";
    }
}
