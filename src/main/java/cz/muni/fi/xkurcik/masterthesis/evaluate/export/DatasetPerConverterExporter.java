package cz.muni.fi.xkurcik.masterthesis.evaluate.export;

import cz.muni.fi.xkurcik.masterthesis.evaluate.EvaluationResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Export results for each converter into separate directory. In the directory it creates .csv file for each dataset.
 * Path format: {converter}/{dataset}.csv
 * In the .csv file there is separate table for each tracker.
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class DatasetPerConverterExporter extends AResultExporter {
    private static final Logger LOGGER = LogManager.getLogger(DatasetPerConverterExporter.class);

    private static final String FILENAME_FORMAT = "%s.csv";
    private static final String CONVERTER_HEADER_NAME = "Converter: %s";
    private static final String DATASET_HEADER_NAME = "Dataset: %s";

    public DatasetPerConverterExporter() {
    }

    @Override
    public void export(List<EvaluationResult> results, Path outputDirectory) throws IOException {
        createOutputDir(outputDirectory);
        Map<String, Map<String, List<EvaluationResult>>> converterMap = parseResults(results, (r) -> r.getCodec().getKey().name());
        for (Map.Entry<String, Map<String, List<EvaluationResult>>> converter : converterMap.entrySet()) {
            exportConverter(converter.getKey(), converter.getValue(), outputDirectory);
        }
    }

    /**
     * Export data for one converter
     */
    private void exportConverter(String name, Map<String, List<EvaluationResult>> datasets, Path outputDirectory) throws IOException {
        LOGGER.info(String.format("Exporting converter %s", name));
        Path converterDirectory = outputDirectory.resolve(name);
        createOutputDir(converterDirectory);
        for (Map.Entry<String, List<EvaluationResult>> dataset : datasets.entrySet()) {
            exportDataset(name, dataset.getKey(), dataset.getValue(), converterDirectory);
        }
    }

    /**
     * Export data for one dataset
     */
    private void exportDataset(String converterName, String datasetName, List<EvaluationResult> results, Path converterDirectory) throws IOException {
        LOGGER.info(String.format("Exporting converter %s - dataset %s", converterName, datasetName));
        Path datasetFile = converterDirectory.resolve(String.format(FILENAME_FORMAT, datasetName));
        Map<String, List<EvaluationResult>> trackers = splitByTracker(results);
        try (
                FileWriter fw = new FileWriter(datasetFile.toString());
                CSVPrinter printer = new CSVPrinter(fw, CSVFormat.EXCEL)
        ) {
            LOGGER.info(String.format("Writing to '%s'", datasetFile.toString()));
            printDatasetHeader(printer, converterName, datasetName);
            for (Map.Entry<String, List<EvaluationResult>> trackerResult : trackers.entrySet()) {
                printTrackerTable(printer, trackerResult.getKey(), trackerResult.getValue());
            }
        }
    }

    /**
     * Split results by tacker name
     */
    private Map<String, List<EvaluationResult>> splitByTracker(List<EvaluationResult> results) {
        Map<String, List<EvaluationResult>> map = new HashMap<>();
        for (EvaluationResult result : results) {
            String tracker = result.getTracker().getName();
            if (!map.containsKey(tracker)) {
                map.put(tracker, new ArrayList<>());
            }
            map.get(tracker).add(result);
        }
        return map;
    }

    /**
     * Print header for the .csv file
     */
    private void printDatasetHeader(CSVPrinter printer, String converterName, String datasetName) throws IOException {
        printer.printRecord(String.format(CONVERTER_HEADER_NAME, converterName));
        printer.printRecord(String.format(DATASET_HEADER_NAME, datasetName));
        printer.printRecord();
    }

    /**
     * Print table for one tracker
     */
    private void printTrackerTable(CSVPrinter printer, String trackerName, List<EvaluationResult> results) throws IOException {
        TrackerTablePrinter trackerTablePrinter = new TrackerTablePrinter(printer);
        trackerTablePrinter.print(trackerName, results, false);
    }
}
