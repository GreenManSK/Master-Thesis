package cz.muni.fi.xkurcik.masterthesis.evaluate;

import cz.muni.fi.xkurcik.masterthesis.cli.Argument;
import cz.muni.fi.xkurcik.masterthesis.cli.ICliRunner;
import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.evaluate.export.CsvConverterExporter;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.TrackerBuilder;
import javafx.util.Pair;
import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for running evaluation on tracker results according to configuration
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class EvaluateRunner implements ICliRunner {
    private static final Logger LOGGER = LogManager.getLogger(EvaluateRunner.class.getName());
    private static final List<Argument> arguments = new ArrayList<>() {
        {
            add(Argument.DATASETS);
            add(Argument.CONVERTED_DIR);
            add(Argument.EVALUATORS);
            add(Argument.RESULTS);
        }
    };

    private Runtime runtime;
    private Config config;
    private ConverterProvider converterProvider;
    private List<Pair<Codec, String>> codecs;

    @Override
    public void run(CommandLine commandLine, Runtime runtime, Config config) {
        this.runtime = runtime;
        this.config = config;
        this.converterProvider = new ConverterProvider(runtime, config);
        this.codecs = Codec.listFromConfig(config);

        Path datasets = Paths.get(Argument.DATASETS.get(commandLine));
        Path target = Paths.get(Argument.CONVERTED_DIR.get(commandLine));
        Path evaluators = Paths.get(Argument.EVALUATORS.get(commandLine));
        Path results = Paths.get(Argument.RESULTS.get(commandLine));

        LOGGER.info("Running evaluate");
        start(datasets, target, evaluators, results);
    }

    /**
     * Export all data using CsvConverterExporter
     *
     * @param datasetsDir          Directory with all datasets
     * @param convertedDatasetsDir Directory with all converted datasets
     * @param evaluatorsDir        Directory with executables for evaluators
     * @param resultsDir           Directory for saving results
     */
    private void start(Path datasetsDir, Path convertedDatasetsDir, Path evaluatorsDir, Path resultsDir) {
        MassEvaluator evaluator = new MassEvaluator(Runtime.getRuntime(), evaluatorsDir, converterProvider);
        List<ITracker> trackers = TrackerBuilder.buildTrackers(runtime, config, Paths.get(""));

        List<EvaluationResult> results = evaluator.evaluate(datasetsDir, config.datasets, convertedDatasetsDir, codecs, trackers);

        CsvConverterExporter csvConverterExporter = new CsvConverterExporter();
        try {
            csvConverterExporter.export(results, resultsDir);
        } catch (IOException e) {
            LOGGER.error("Error while exporting results", e);
        }
    }

    @Override
    public List<Argument> getNeededArguments() {
        return arguments;
    }
}
