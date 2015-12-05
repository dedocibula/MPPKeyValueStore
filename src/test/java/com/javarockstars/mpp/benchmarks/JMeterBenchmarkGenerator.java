package com.javarockstars.mpp.benchmarks;

import com.javarockstars.mpp.benchmarks.keyvaluestore.KeyValueStoreOperation;
import com.javarockstars.mpp.benchmarks.keyvaluestore.KeyValueStoreOperationGenerator;
import com.javarockstars.mpp.benchmarks.keyvaluestore.KeyValueStoreOperationGenerator.Pair;

import java.util.List;
import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 4.12.2015.
 */
public class JMeterBenchmarkGenerator {
    public static void main(String[] args) {
        KeyValueStoreOperationGenerator.generate(90, 10, 0);
        dump(KeyValueStoreOperationGenerator.retrieve(90, 10, 0));

        KeyValueStoreOperationGenerator.generate(50, 30, 20);
        dump(KeyValueStoreOperationGenerator.retrieve(50, 30, 20));
    }

    private static void dump(final List<Pair<KeyValueStoreOperation, String>> pairs) {
        Objects.requireNonNull(pairs);
        pairs.forEach(p -> System.out.println(p.first + " " + p.second));
        System.out.println();
    }
}
