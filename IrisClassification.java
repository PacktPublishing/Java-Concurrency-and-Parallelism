package com.example;

import java.io.File;
import java.io.IOException;
import org.deeplearning4j.datasets.iterator.impl.IrisDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class IrisClassification {
    public static void main(String[] args) throws IOException {
        // Load the Iris dataset
        DataSetIterator irisIter = new IrisDataSetIterator(150, 150);

        // Build the neural network
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new Adam(0.01))
                .list()
                .layer(new DenseLayer.Builder().nIn(4).nOut(10).activation(Activation.RELU).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX).nIn(10).nOut(3).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(10));

        // Train the model
        model.fit(irisIter);

        // Evaluate the model and save to iris-model.zip
        Evaluation eval = model.evaluate(irisIter);
        System.out.println(eval.stats());

        // Save the model to 
        ModelSerializer.writeModel(model, new File("iris-model.zip"), true);
    }
}
