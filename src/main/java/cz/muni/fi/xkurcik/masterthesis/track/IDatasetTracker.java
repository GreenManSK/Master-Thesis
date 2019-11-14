package cz.muni.fi.xkurcik.masterthesis.track;

import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;
import javafx.util.Pair;

import java.nio.file.Path;
import java.util.List;

/**
 * Use all trackers on all converted version of specified dataset
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface IDatasetTracker {
    /**
     * Run each tracker on each converted version of dataset. Results are saved to {SEQUENCE_NUMBER}_RES_{TRACKER_NAME}.
     *
     * @param datasetName          Name of the original dataset
     * @param datasetPath          Path to the original dataset
     * @param convertedDatasetsDir Directory with converted versions of dataset
     * @param codecs               List of codecs with parameters used for conversion
     * @param trackersDir          Directory with all the trackers
     * @param trackers             List of trackers
     */
    void track(
            String datasetName,
            Path datasetPath,
            Path convertedDatasetsDir,
            List<Pair<Codec, ?>> codecs,
            Path trackersDir,
            List<ITracker> trackers
    );
}
