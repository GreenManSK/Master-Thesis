package cz.muni.fi.xkurcik.masterthesis.evaluate;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.evaluate.export.CsvConverterExporter;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.TrackerBuilder;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Class for running evaluation on tracker results according to configuration
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class EvaluateRunner {
    private static final Logger LOGGER = LogManager.getLogger(EvaluateRunner.class.getName());

    private Runtime runtime;
    private Config config;
    private ConverterProvider converterProvider;
    private List<Pair<Codec, String>> codecs;

    public EvaluateRunner(Runtime runtime, Config config) {
        this.runtime = runtime;
        this.config = config;
        this.converterProvider = new ConverterProvider(runtime, config);
        this.codecs = Codec.listFromConfig(config);
    }

    public void run(Path datasetsDir, Path convertedDatasetsDir, Path evaluatorsDir, Path resultsDir) {
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
}
