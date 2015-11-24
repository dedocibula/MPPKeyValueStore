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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Author: dedocibula
 * Created on: 23.11.2015.
 */
public class TabularCSVOutput extends AbstractOutput {
    private transient final PrintStream out;

    private static final int COLUMN_COUNT = 9;
    private static final String SEPARATOR = ",";

    private boolean firstResult;

    public TabularCSVOutput(final PrintStream out) {
        super();
        Objects.requireNonNull(out);
        this.out = out;
        this.firstResult = true;
    }

    public TabularCSVOutput() {
        this(System.out);
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
            addHeader(meter.getName(), Alignment.Center);
            for (final ClassResult classRes : res.getIncludedResults()) {
                addHeader(classRes.getElementName(), Alignment.Left);
                for (final MethodResult methRes : classRes.getIncludedResults()) {
                    generateMeterResult(methRes.getElementName(), meter, methRes);
                }

                addHeader("Summary for " + classRes.getElementName(), Alignment.Left);
                generateMeterResult("", meter, classRes);
                emptyRow();
            }
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
}
