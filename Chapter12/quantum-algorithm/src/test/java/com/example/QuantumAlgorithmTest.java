package com.example;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redfx.strange.Program;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

public class QuantumAlgorithmTest {

	@Mock
	private SimpleQuantumExecutionEnvironment simulator;

	@Mock
	private Result result;

	@Mock
	private Qubit qubit;

	@InjectMocks
	private QuantumAlgorithm quantumAlgorithm;

	@Before
	public void setup() {
		MockitoAnnotations.openMocks(this);

		// Mock behavior for the quantum execution environment
		when(simulator.runProgram(any(Program.class))).thenReturn(result);
		when(result.getQubits()).thenReturn(new Qubit[] { qubit });
	}

	@Test
	public void testSingleRunMeasurement() {
		when(qubit.measure()).thenReturn(0);

		// Run the main method
		QuantumAlgorithm.main(new String[] {});

		// Verify that the simulator was called
		verify(simulator, atLeastOnce()).runProgram(any(Program.class));
	}

	@Test
	public void testMultipleRunMeasurements() {
		when(qubit.measure()).thenReturn(0).thenReturn(1);

		// Run the main method
		QuantumAlgorithm.main(new String[] {});

		// Verify that the simulator was called multiple times
		verify(simulator, atLeast(1001)).runProgram(any(Program.class)); // 1 initial run + 1000 in the loop
	}
}
