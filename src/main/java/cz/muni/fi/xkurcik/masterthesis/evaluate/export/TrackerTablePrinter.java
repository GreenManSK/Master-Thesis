package cz.muni.fi.xkurcik.masterthesis.evaluate.export;

import cz.muni.fi.xkurcik.masterthesis.evaluate.EvaluationResult;
import cz.muni.fi.xkurcik.masterthesis.evaluate.evaluators.Evaluator;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Prints table of results for one tracker used on datasets converted by one converter
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class TrackerTablePrinter {

    private CSVPrinter printer;

    public TrackerTablePrinter(CSVPrinter printer) {
        this.printer = printer;
    }

    /**
     * @TODO
     */
    public void print(String trackerName, List<EvaluationResult> results) throws IOException {
        // Split by params
        Map<String, List<EvaluationResult>> paramMap = splitByParams(results);
        // Get list of sequence names
        List<String> sequences = getSequenceList(paramMap);
        // Print header
        printHeader(trackerName, sequences);
        // Print param row
        for (Map.Entry<String, List<EvaluationResult>> paramRow : paramMap.entrySet()) {
            printRow(paramRow.getKey(), paramRow.getValue());
        }

        printer.printRecord();
    }

    /**
     * Print table header
     */
    private void printHeader(String trackerName, List<String> sequences) throws IOException {
        printer.print(trackerName);

        for (String sequence : sequences) {
            for (Evaluator evaluator : Evaluator.values()) {
                printer.print(String.format("%s %s", sequence, evaluator.toString()));
            }
        }

        printer.println();
    }

    /**
     * Print table row
     */
    private void printRow(String prams, List<EvaluationResult> results) throws IOException {
        printer.print(prams);

        for (EvaluationResult result : results) {
            for (Evaluator evaluator : Evaluator.values()) {
                printer.print(result.get(evaluator).trim());
            }
        }

        printer.println();
    }

    /**
     * Split results by parameters
     */
    private Map<String, List<EvaluationResult>> splitByParams(List<EvaluationResult> results) {
        Map<String, List<EvaluationResult>> paramMap = new HashMap<>();
        for (EvaluationResult result : results) {
            String param = result.getCodec().getValue();
            if (!paramMap.containsKey(param)) {
                paramMap.put(param, new ArrayList<>());
            }
            paramMap.get(param).add(result);
        }
        return paramMap;
    }

    /**
     * Return list of sequence names
     */
    private List<String> getSequenceList(Map<String, List<EvaluationResult>> paramMap) {
        List<String> sequences = new ArrayList<>();
        List<EvaluationResult> firstParam = paramMap.entrySet().iterator().next().getValue();
        for (EvaluationResult result : firstParam) {
            sequences.add(result.getSequence());
        }
        return sequences;
    }
}
