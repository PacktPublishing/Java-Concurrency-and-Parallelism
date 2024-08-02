package com.example;

import org.redfx.strange.Program;
import org.redfx.strange.QuantumExecutionEnvironment;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

public class QuantumAlgorithm {

    public static void main(String[] args) {
        // Create a program with 1 qubit
        Program program = new Program(1);

        // Add a Hadamard gate to create superposition
        Step step = new Step();
        step.addGate(new Hadamard(0));
        program.addStep(step);

        // Create a quantum execution environment
        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();

        // Run the program once and measure
        Result result = simulator.runProgram(program);
        Qubit[] qubits = result.getQubits();
        System.out.println("Single run measurement: " + qubits[0].measure());

        // Run the program multiple times to see the distribution
        int runs = 1000;
        int[] measurements = new int[2];
        for (int i = 0; i < runs; i++) {
            result = simulator.runProgram(program);
            qubits = result.getQubits();
            measurements[qubits[0].measure()]++;
        }

        System.out.println("\nAfter " + runs + " runs:");
        System.out.println("Measured 0: " + measurements[0] + " times");
        System.out.println("Measured 1: " + measurements[1] + " times");
    }
}
