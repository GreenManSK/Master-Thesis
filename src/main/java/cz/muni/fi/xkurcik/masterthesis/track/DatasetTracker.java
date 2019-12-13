package cz.muni.fi.xkurcik.masterthesis.track;

import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.IConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.helpers.DatasetHelper;
import cz.muni.fi.xkurcik.masterthesis.helpers.NamingHelper;
import cz.muni.fi.xkurcik.masterthesis.helpers.SymlinkHelper;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Basic implementation of IDatasetTracker
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class DatasetTracker implements IDatasetTracker {
    private static final Logger LOGGER = LogManager.getLogger(DatasetTracker.class);

    private ConverterProvider converterProvider;

    public DatasetTracker(ConverterProvider converterProvider) {
        this.converterProvider = converterProvider;
    }

    @Override
    public void track(String datasetName, Path datasetPath, Path convertedDatasetsDir, List<Pair<Codec, String>> codecs, Path trackersDir, List<ITracker> trackers) {
        LOGGER.info(String.format("Starting tracking for %s", datasetName));

        int sequences = DatasetHelper.getNumberOfSequences(datasetPath);
        if (sequences == 0) {
            LOGGER.warn(String.format("No sequence for '%s' exists", datasetName));
            return;
        }

        int filenameLength = DatasetHelper.getFilenameLength(datasetPath);
        if (filenameLength == 0) {
            LOGGER.warn(String.format("Invalid filenames in '%s'", datasetName));
            return;
        }

        for (Pair<Codec, ?> codec : codecs) {
            LOGGER.info(String.format("Tracking %s converted by %s", datasetName, codec.getKey().toString()));
            trackCodec(datasetName, convertedDatasetsDir, codec.getKey(), codec.getValue(), sequences, filenameLength, trackersDir, trackers);
        }
    }

    /**
     * Run all trackers on dataset converted using provided codec
     */
    private void trackCodec(String datasetName, Path datasetsDir, Codec codec, Object codecParams, int sequences, int filenameLength, Path trackersDir, List<ITracker> trackers) {
        IConverter converter = converterProvider.getByCodec(codec);
        String convertedDatasetName = NamingHelper.createDatasetName(datasetName, codec, codecParams, converter);

        Path convertedDatasetPath = datasetsDir.resolve(convertedDatasetName);
        if (!Files.exists(convertedDatasetPath)) {
            LOGGER.info(String.format("Skipping %s converted by %s - dose not exist", datasetName, codec.toString()));
        }
        Path symlinkPath = trackersDir.resolve(datasetName);
        try {
            SymlinkHelper.createSymlink(symlinkPath, convertedDatasetPath);

            List<String> sequencesList = NamingHelper.getSequenceStrings(sequences);
            for (ITracker tracker : trackers) {
                LOGGER.info(String.format("Tracking %s converted by %s by tracker %s", datasetName, codec.toString(), tracker.getName()));
                trackTracker(datasetName, filenameLength, symlinkPath, sequencesList, tracker);
            }

            SymlinkHelper.deleteSymlink(symlinkPath);
        } catch (IOException e) {
            LOGGER.error(String.format("Problem while tracking %s", datasetName), e);
        }
    }

    /**
     * Track with tracker
     */
    private void trackTracker(String datasetName, int filenameLength, Path symlinkPath, List<String> sequencesList, ITracker tracker) throws IOException {
        for (String sequence : sequencesList) {
            tracker.run(datasetName, sequence, filenameLength);
            Path resultDir = symlinkPath.resolve(NamingHelper.getResultFolderName(sequence));
            if (Files.exists(resultDir)) {
                Files.move(
                        resultDir,
                        symlinkPath.resolve(NamingHelper.getResultFolderName(sequence, tracker))
                );
            } else {
                LOGGER.error(String.format("Result folder was not created for %s by tracker %s sequence %s", datasetName, tracker.getName(), sequence));
            }
        }
    }
}
