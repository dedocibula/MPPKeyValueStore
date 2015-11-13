package com.javarockstars.mpp.benchmark.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.perfidix.AbstractConfig;
import org.perfidix.annotation.BenchmarkConfig;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.ouput.CSVOutput;

/**
 * This class is responsible for setting up the default values of configuration
 * parameters for the benchmarking.
 * 
 * @author shivam-maharshi
 */
@BenchmarkConfig
public class CustomConfig extends AbstractConfig {

	private static int RUNS = 50;
	private static Set<AbstractMeter> TIME_METER = new HashSet<AbstractMeter>();
	private static KindOfArrangement ARRANGEMENT = KindOfArrangement.NoArrangement;
	private static double GARBAGE_COLLECTION = 1;

	public static Set<AbstractOutput> getOutput(File file) {
		Set<AbstractOutput> OUTPUT = new HashSet<AbstractOutput>();
		OUTPUT.add(new CSVOutput(file));
		return OUTPUT;
	}

	public CustomConfig() {
		super(RUNS, TIME_METER, getOutput(new File("C:\\Users\\Sam\\Workspace\\MPPKeyValueStore\\result")), ARRANGEMENT,
				GARBAGE_COLLECTION);
	}

}
