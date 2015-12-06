package com.javarockstars.mpp.benchmarks;

import org.perfidix.AbstractConfig;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.meter.*;
import org.perfidix.ouput.AbstractOutput;

import java.util.HashSet;
import java.util.Set;

/**
 * Custom config for benchmarks.
 * <p>
 * Author: dedocibula
 * Created on: 24.11.2015.
 */
@org.perfidix.annotation.BenchmarkConfig
public class BenchmarkConfig extends AbstractConfig {
    private final static int RUNS = 10;
    private final static Set<AbstractMeter> METERS = new HashSet<>();
    private final static Set<AbstractOutput> OUTPUT = new HashSet<>();

    private final static KindOfArrangement ARRANGEMENT = KindOfArrangement.SequentialMethodArrangement;
    private final static double GCPROB = 1.0d;

    static {
        METERS.add(new TimeMeter(Time.MilliSeconds));
        METERS.add(new MemMeter(Memory.Mebibyte));
    }

    public BenchmarkConfig() {
        super(RUNS, METERS, OUTPUT, ARRANGEMENT, GCPROB);
    }
}
