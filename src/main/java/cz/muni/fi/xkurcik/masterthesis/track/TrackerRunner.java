package cz.muni.fi.xkurcik.masterthesis.track;

import cz.muni.fi.xkurcik.masterthesis.cli.Argument;
import cz.muni.fi.xkurcik.masterthesis.cli.ICliRunner;
import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.TrackerBuilder;
import javafx.util.Pair;
import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class running trackers on converted datasets according to configuration
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class TrackerRunner implements ICliRunner {
    private static final Logger LOGGER = LogManager.getLogger(TrackerRunner.class.getName());
    private static final List<Argument> arguments = new ArrayList<>() {
        {
            add(Argument.DATASETS);
            add(Argument.CONVERTED_DIR);
            add(Argument.TRACKERS_DIR);
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
        Path trackers = Paths.get(Argument.TRACKERS_DIR.get(commandLine));

        LOGGER.info("Running tracker");
        start(datasets, target, trackers);
    }

    /**
     * Run configured trackers on the converted datasets
     *
     * @param datasetsDir          Directory with all datasets
     * @param convertedDatasetsDir Directory with all converted datasets
     * @param trackersDir          Directory with trackers executables
     */
    private void start(Path datasetsDir, Path convertedDatasetsDir, Path trackersDir) {
        DatasetTracker tracker = new DatasetTracker(converterProvider);
        List<ITracker> trackers = TrackerBuilder.buildTrackers(runtime, config, trackersDir);
        for (String datasetName : config.datasets) {
            tracker.track(datasetName, datasetsDir.resolve(datasetName), convertedDatasetsDir, codecs, trackersDir, trackers);
        }
    }

    @Override
    public List<Argument> getNeededArguments() {
        return arguments;
    }
}
