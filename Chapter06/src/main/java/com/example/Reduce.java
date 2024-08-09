package com.example;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reduce extends Reducer<Text, Text, Text, Text> {

  @Override
  public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
    StringBuilder sessionSequence = new StringBuilder();

    // Iterate through visited URLs within the same session (defined by key)
    for (Text url : values) {
      sessionSequence.append(url.toString()).append(" -> ");
    }

    // Remove the trailing " -> " from the sequence
    if (sessionSequence.length() > 4) {
      sessionSequence.setLength(sessionSequence.length() - 4);
    }

    // Emit key-value pair: (sessionKey, sequence of visited URLs)
    context.write(key, new Text(sessionSequence.toString()));
  }
}
