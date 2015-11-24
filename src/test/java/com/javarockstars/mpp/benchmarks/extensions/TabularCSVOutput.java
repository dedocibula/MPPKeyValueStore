package com.javarockstars.mpp.benchmarks.extensions;

import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.exceptions.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.ouput.asciitable.AbstractTabularComponent.Alignment;
import org.perfidix.result.AbstractResult;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Author: dedocibula
 * Created on: 23.11.2015.
 */
public final class TabularCSVOutput extends AbstractOutput {
    private transient final PrintStream out;

    private static final int COLUMN_COUNT = 9;
    private static final String SEPARATOR = ",";

    private boolean firstResult;
    private Grouping resultGrouping;

    private TabularCSVOutput(final PrintStream out) {
        super();
        Objects.requireNonNull(out);
        this.out = out;
        this.firstResult = true;
        this.resultGrouping = Grouping.CLASS;
    }

    public static TabularCSVOutput toConsole() {
        return new TabularCSVOutput(System.out);
    }

    public static TabularCSVOutput toStream(final PrintStream out) {
        return new TabularCSVOutput(out);
    }

    public TabularCSVOutput groupBy(final Grouping resultGrouping) {
        Objects.requireNonNull(resultGrouping);
        this.resultGrouping = resultGrouping;
        return this;
    }

    @Override
    public void visitBenchmark(BenchmarkResult res) {
        Objects.requireNonNull(res);

        // generate header
        addHeader("BENCHMARK", Alignment.Center);
        emptyRow();
        addRow("-", "unit", "sum", "min", "max", "avg", "stddev", "conf95", "runs");

        // generate metrics by benchmark
        for (final AbstractMeter meter : res.getRegisteredMeters()) {
            if (resultGrouping == Grouping.METHOD)
                generateMetricsByMethod(meter, res);
            else
                generateMetricsByClass(meter, res);
        }

        // generate final summary
        addHeader("Summary for the whole benchmark", Alignment.Left);
        for (final AbstractMeter meter : res.getRegisteredMeters()) {
            generateMeterResult("", meter, res);
        }

        // generate potential exceptions
        addHeader("Exceptions", Alignment.Left);
        for (final AbstractPerfidixMethodException exec : res.getExceptions()) {
            addHeader("Related exception: " + exec.getExec().getClass().getSimpleName(), Alignment.Left);

            final StringBuilder execBuilder1 = new StringBuilder();
            if (exec instanceof PerfidixMethodInvocationException) {
                execBuilder1.append("Related place: method invocation");
            } else {
                execBuilder1.append("Related place: method check");
            }
            addHeader(execBuilder1.toString(), Alignment.Left);
            if (exec.getMethod() != null) {
                addHeader("Related method: " + exec.getMethod().getName(), Alignment.Left);
            }
            addHeader("Related annotation: " + exec.getRelatedAnno().getSimpleName(), Alignment.Left);
            emptyRow();

        }
        emptyRow();
    }

    @Override
    public boolean listenToResultSet(Method meth, AbstractMeter meter, double data) {
        Objects.requireNonNull(meth);
        Objects.requireNonNull(meter);
        if (!firstResult)
            out.print(SEPARATOR);
        addRow("Class", meth.getDeclaringClass().getSimpleName() + "#" + meth.getName());
        addRow("Meter", meter.getName());
        addRow("Data", Double.toString(data));
        firstResult = false;
        return true;
    }

    @Override
    public boolean listenToException(AbstractPerfidixMethodException exec) {
        if (exec.getMethod() != null)
            addRow("Class", exec.getMethod().getDeclaringClass().getSimpleName() + "#" + exec.getMethod().getName());
        addRow("Annotation", exec.getRelatedAnno().getSimpleName());
        addRow("Exception", exec.getClass().getSimpleName() + "/" + exec.getExec().toString());
        exec.getExec().printStackTrace(out);
        emptyRow();
        return true;
    }

    private void generateMetricsByClass(final AbstractMeter meter, final BenchmarkResult result) {
        for (final ClassResult classRes : result.getIncludedResults()) {
            addHeader(classRes.getElementName(), Alignment.Left);
            for (final MethodResult methRes : classRes.getIncludedResults()
                    .stream()
                    .sorted((m1, m2) -> m1.getElementName().compareTo(m2.getElementName()))
                    .collect(Collectors.toList())) {
                generateMeterResult(methRes.getElementName(), meter, methRes);
            }

            addHeader("Summary for " + classRes.getElementName(), Alignment.Left);
            generateMeterResult("", meter, classRes);
            emptyRow();
        }
    }

    private void generateMetricsByMethod(final AbstractMeter meter, final BenchmarkResult result) {
        Map<String, List<Pair<MethodResult, ClassResult>>> resultListMap = result.getIncludedResults()
                .stream()
                .flatMap(c -> c.getIncludedResults().stream().map(m -> new Pair<>(m, c)))
                .collect(Collectors.groupingBy(p -> p.first.getElementName()));
        for (final String methName : resultListMap.keySet()) {
            addHeader(methName.toUpperCase(), Alignment.Left);
            for (Pair<MethodResult, ClassResult> pair : resultListMap.get(methName)
                    .stream()
                    .sorted((p1, p2) -> p1.second.getElementName().compareTo(p2.second.getElementName()))
                    .collect(Collectors.toList())) {
                generateMeterResult(pair.second.getElementName(), meter, pair.first);
            }

            emptyRow();
        }
    }

    private void generateMeterResult(final String columnDesc, final AbstractMeter meter, final AbstractResult result) {
        addRow(columnDesc,
                meter.getUnit(),
                AbstractOutput.format(result.sum(meter)),
                AbstractOutput.format(result.min(meter)),
                AbstractOutput.format(result.max(meter)),
                AbstractOutput.format(result.mean(meter)),
                AbstractOutput.format(result.getStandardDeviation(meter)),
                "[" + AbstractOutput.format(result.getConf05(meter)) + "-" + AbstractOutput.format(result.getConf95(meter)) + "]",
                AbstractOutput.format(result.getResultSet(meter).size()));
    }

    private void addHeader(final String title, final Alignment alignment) {
        int position = alignment == Alignment.Center ? COLUMN_COUNT / 2 : (alignment == Alignment.Right) ? COLUMN_COUNT - 1 : 0;
        for (int i = 0; i < COLUMN_COUNT; i++) {
            if (i == position)
                out.print(title);
            out.print(SEPARATOR);
        }
        out.println();
    }

    private void addRow(final String... data) {
        out.println(Arrays.stream(data).collect(Collectors.joining(SEPARATOR)));
    }

    private void emptyRow() {
        out.println();
    }

    public enum Grouping {
        CLASS,
        METHOD
    }

    private static final class Pair<T1, T2> {
        final T1 first;
        final T2 second;

        private Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }
    }
}
