package com.example;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class LogMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Split the log line based on delimiters (e.g., comma)
        String[] logData = value.toString().split(",");

        // Extract user ID, timestamp, and webpage URL
        String userId = logData[0];
        long timestamp = Long.parseLong(logData[1]);
        String url = logData[2];

        // Combine user ID and timestamp (key for grouping by session)
        String sessionKey = userId + "-" + String.valueOf(timestamp / (15 * 60 * 1000));

        // Emit key-value pair: (sessionKey, URL)
        context.write(new Text(sessionKey), new Text(url));
    }
}
