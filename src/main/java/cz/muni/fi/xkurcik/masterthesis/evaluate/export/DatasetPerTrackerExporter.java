package cz.muni.fi.xkurcik.masterthesis.evaluate.export;

import cz.muni.fi.xkurcik.masterthesis.evaluate.EvaluationResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Export results for each tracker into separate directory. In the directory it creates .csv files for each dataset.
 * Path format: {tracker}/{dataset}.csvň
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class DatasetPerTrackerExporter extends AResultExporter {
    private static final Logger LOGGER = LogManager.getLogger(DatasetPerTrackerExporter.class);

    private static final String FILENAME_FORMAT = "%s.csv";
    private static final String TRACKER_HEADER_NAME = "Tracker: %s";
    private static final String DATASET_HEADER_NAME = "Dataset: %s";
    private static final String CONVERTER_COLUMN_NAME = "Converter";


    public DatasetPerTrackerExporter() {
    }

    @Override
    public void export(List<EvaluationResult> results, Path outputDirectory) throws IOException {
        createOutputDir(outputDirectory);

        Map<String, Map<String, List<EvaluationResult>>> converterMap = parseResults(results, (r) -> r.getTracker().getName());
        for (Map.Entry<String, Map<String, List<EvaluationResult>>> converter : converterMap.entrySet()) {
            exportTracker(converter.getKey(), converter.getValue(), outputDirectory);
        }
    }

    /**
     * Export data for one tracker
     */
    private void exportTracker(String name, Map<String, List<EvaluationResult>> datasets, Path outputDirectory) throws IOException {
        LOGGER.info(String.format("Exporting tracker %s", name));
        Path trackerDirectory = outputDirectory.resolve(name);
        createOutputDir(trackerDirectory);
        for (Map.Entry<String, List<EvaluationResult>> dataset : datasets.entrySet()) {
            exportDataset(name, dataset.getKey(), dataset.getValue(), trackerDirectory);
        }
    }

    /**
     * Export data for one dataset
     */
    private void exportDataset(String trackerName, String datasetName, List<EvaluationResult> results, Path trackerDirectory) throws IOException {
        LOGGER.info(String.format("Exporting tracker %s - dataset %s", trackerName, datasetName));

        Path datasetFile = trackerDirectory.resolve(String.format(FILENAME_FORMAT, datasetName));

        results.sort((a, b) -> {
            int diff = a.getCodec().getKey().compareTo(b.getCodec().getKey());
            if (diff == 0) {
                return a.getCodec().getValue().compareTo(b.getCodec().getValue());
            }
            return diff;
        });

        try (
                FileWriter fw = new FileWriter(datasetFile.toString());
                CSVPrinter printer = new CSVPrinter(fw, CSVFormat.EXCEL)
        ) {
            LOGGER.info(String.format("Writing to '%s'", datasetFile.toString()));
            printDatasetHeader(printer, trackerName, datasetName);
            printTable(printer, results);
        }
    }

    /**
     * Print header for the .csv file
     */
    private void printDatasetHeader(CSVPrinter printer, String trackerName, String datasetName) throws IOException {
        printer.printRecord(String.format(TRACKER_HEADER_NAME, trackerName));
        printer.printRecord(String.format(DATASET_HEADER_NAME, datasetName));
        printer.printRecord();
    }

    /**
     * Print table
     */
    private void printTable(CSVPrinter printer, List<EvaluationResult> results) throws IOException {
        TrackerTablePrinter trackerTablePrinter = new TrackerTablePrinter(printer);
        trackerTablePrinter.print(CONVERTER_COLUMN_NAME, results, true);
    }
}
