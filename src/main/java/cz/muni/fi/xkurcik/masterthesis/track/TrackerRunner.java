package cz.muni.fi.xkurcik.masterthesis.track;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.TrackerBuilder;
import javafx.util.Pair;

import java.nio.file.Path;
import java.util.List;

/**
 * Class running trackers on converted datasets according to configuration
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 * @TODO: Comments
 */
public class TrackerRunner {
    private Runtime runtime;
    private Config config;
    private ConverterProvider converterProvider;
    private List<Pair<Codec, String>> codecs;

    public TrackerRunner(Runtime runtime, Config config) {
        this.runtime = runtime;
        this.config = config;
        this.converterProvider = new ConverterProvider(runtime, config);
        this.codecs = Codec.listFromConfig(config);
    }

    public void run(Path datasetsDir, Path convertedDatasetsDir, Path trackersDir) {
        DatasetTracker tracker = new DatasetTracker(converterProvider);
        List<ITracker> trackers = TrackerBuilder.buildTrackers(runtime, config, trackersDir);
        for (String datasetName : config.datasets) {
            tracker.track(datasetName, datasetsDir.resolve(datasetName), convertedDatasetsDir, codecs, trackersDir, trackers);
        }
    }
}
