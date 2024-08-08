package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class DataProcessor extends AbstractBehavior<DataProcessor.DataCommand> {

    interface DataCommand {
    }

    static final class ProcessData implements DataCommand {
        final String content;

        ProcessData(String content) {
            this.content = content;
        }
    }

    static final class DataResult implements DataCommand {
        final String result;

        DataResult(String result) {
            this.result = result;
        }
    }

    static Behavior<DataCommand> create() {
        return Behaviors.setup(DataProcessor::new);
    }

    private DataProcessor(ActorContext<DataCommand> context) {
        super(context);
    }

    @Override
    public Receive<DataCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(ProcessData.class, this::onProcessData)
                .onMessage(DataResult.class, this::onDataResult)
                .build();
    }

    private Behavior<DataCommand> onProcessData(ProcessData data) {
        try {
            getContext().getLog().info("Processing data: {}", data.content);
            // Data processing logic here
            @SuppressWarnings("unused")
            DataResult result = new DataResult("Processed: " + data.content);
            return this;
        } catch (Exception e) {
            getContext().getLog().error("Error processing data: {}", data.content, e);
            return Behaviors.stopped();
        }
    }

    private Behavior<DataCommand> onDataResult(DataResult result) {
        // Handle DataResult if needed
        return this;
    }
}