package com.javarockstars.mpp.benchmarks.keyvaluestore;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Author: dedocibula
 * Created on: 4.12.2015.
 */
public final class KeyValueStoreOperationGenerator {
    private static final Random random = new Random();
    private static final int operationCount = 10000;
    private static final float successRatio = .75f;
    private static final String fileNamePrefix = "jmeter-benchmark-";

    public static List<Pair<KeyValueStoreOperation, String>> retrieve(int gets, int adds, int deletes) {
        validate(gets, adds, deletes);
        final String fileName = String.format("%s%s_%s_%s", fileNamePrefix, gets, adds, deletes);
        List<Pair<KeyValueStoreOperation, String>> pairs = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(",");
                pairs.add(new Pair<>(KeyValueStoreOperation.valueOf(parts[0]), parts[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pairs;
    }

    public static void generate(int gets, int adds, int deletes) {
        validate(gets, adds, deletes);
        List<Pair<KeyValueStoreOperation, String>> pairs = doMagic(gets, adds, deletes);
        final String fileName = String.format("%s%s_%s_%s", fileNamePrefix, gets, adds, deletes);
        try (OutputStreamWriter s = outputStream(fileName)) {
            for (Pair<KeyValueStoreOperation, String> pair : pairs)
                s.write(String.format("%s,%s\n", pair.first, pair.second));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Pair<KeyValueStoreOperation, String>> doMagic(int gets, int adds, int deletes) {
        float percent = operationCount / 100f;
        int[] operations = {(int) (gets * percent), (int) (adds * percent), (int) (deletes * percent)};
        int[] getDistribution = distribute(operations, 0), addDistribution = distribute(operations, 1), deleteDistribution = distribute(operations, 2);
        int[] getKeys = Arrays.copyOf(addDistribution, 2);
        List<String> addKeys = IntStream.range(0, addDistribution[0] + addDistribution[1])
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());
        List<String> deleteKeys = IntStream.range(0, deleteDistribution[0] + deleteDistribution[1])
                .mapToObj(i -> (i < deleteDistribution[0]) ? Integer.toString(i) : Integer.toString(addDistribution[0] + i))
                .collect(Collectors.toList());

        int total = operationCount;
        List<Pair<KeyValueStoreOperation, String>> result = new ArrayList<>(operationCount);
        while (total > 0) {
            int choice = random.nextInt(total);
            if (choice < operations[0]) {
                result.add(getPair(getDistribution, getKeys));
                operations[0]--;
            } else if (choice < operations[0] + operations[1]) {
                result.add(addOrDeletePair(KeyValueStoreOperation.ADD, addDistribution, addKeys));
                operations[1]--;
            } else if (choice < operations[0] + operations[1] + operations[2]) {
                result.add(addOrDeletePair(KeyValueStoreOperation.DELETE, deleteDistribution, deleteKeys));
                operations[2]--;
            } else {
                throw new IllegalStateException();
            }
            total--;
        }

        return result;
    }

    private static int[] distribute(final int[] operations, int index) {
        int total = operations[index], success = (int) Math.ceil(total * successRatio);
        return new int[]{success, total - success};
    }

    private static Pair<KeyValueStoreOperation, String> getPair(final int[] distribution,
                                                                final int[] keys) {
        int bound = distribution[0] + distribution[1];
        int operation = bound > 0 ? random.nextInt(bound) : bound;
        if (operation < distribution[0]) {
            distribution[0]--;
            return new Pair<>(KeyValueStoreOperation.GET, Integer.toString(keys[0] > 0 ? random.nextInt(keys[0]) : 0));
        } else {
            distribution[1]--;
            return new Pair<>(KeyValueStoreOperation.GET, Integer.toString(keys[0] + (keys[1] > 0 ? random.nextInt(keys[1]) : 0)));
        }
    }

    private static Pair<KeyValueStoreOperation, String> addOrDeletePair(final KeyValueStoreOperation operation,
                                                                        final int[] distribution,
                                                                        final List<String> keys) {
        int bound = distribution[0] + distribution[1];
        int current = bound > 0 ? random.nextInt(bound) : 0;
        if (current < distribution[0])
            distribution[0]--;
        else
            distribution[1]--;
        return new Pair<>(operation, keys.remove(current));
    }

    private static OutputStreamWriter outputStream(final String fileName) throws IOException {
        Objects.requireNonNull(fileName);
        File currentDirectory = new File(System.getProperty("user.dir"));
        File newFile = new File(currentDirectory, fileName);
        if (newFile.exists())
            return new OutputStreamWriter(newFile.isDirectory() || !newFile.canWrite() ? System.out : new FileOutputStream(newFile));
        else
            return new OutputStreamWriter(newFile.createNewFile() ? new FileOutputStream(newFile) : System.out);
    }

    private static void validate(int gets, int adds, int deletes) {
        if (gets < 0 || gets > 100 ||
                adds < 0 || adds > 100 ||
                deletes < 0 || deletes > 100 ||
                (gets + adds + deletes) != 100)
            throw new IllegalArgumentException("Illegal parameters");
    }

    public static final class Pair<T1, T2> {
        public T1 first;
        public T2 second;

        Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }
    }
}
