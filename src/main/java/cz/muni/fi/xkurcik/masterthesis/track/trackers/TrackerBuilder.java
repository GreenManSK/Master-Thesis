package cz.muni.fi.xkurcik.masterthesis.track.trackers;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.config.TrackerConfig;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds trackers from configuration
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 * @TODO: Comments
 */
public class TrackerBuilder {
    public static List<ITracker> buildTrackers(Runtime runtime, Config config, Path trackersDir) {
        List<ITracker> trackers = new ArrayList<>();
        for (TrackerConfig trackerConfig : config.trackers) {
            Path trackerPath = trackersDir.resolve(trackerConfig.name).resolve(trackerConfig.executableFile);
            ITracker tracker = new SimpleTracker(runtime, trackerConfig.name, trackerPath);
            trackers.add(tracker);
        }
        return trackers;
    }
}
