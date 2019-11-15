package cz.muni.fi.xkurcik.masterthesis.track;

import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.IConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Format;
import cz.muni.fi.xkurcik.masterthesis.helpers.NamingHelper;
import cz.muni.fi.xkurcik.masterthesis.helpers.SymlinkHelper;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;
import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

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
        // Get number of sequences
        int sequences = getNumberOfSequences(datasetPath);
        if (sequences == 0) {
            LOGGER.warn(String.format("No sequence for '%s' exists", datasetName));
            return;
        }

        // Get length for image names
        int filenameLength = getFilenameLength(datasetPath);
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
        // @TODO: Refactor/Split
        IConverter converter = converterProvider.getByCodec(codec);
        String convertedDatasetName = NamingHelper.createDatasetName(datasetName, codec, codecParams, converter);

        Path convertedDatasetPath = datasetsDir.resolve(convertedDatasetName);
        Path symlinkPath = trackersDir.resolve(datasetName);
        try {
            SymlinkHelper.createSymlink(symlinkPath, convertedDatasetPath);

            List<String> sequencesList = NamingHelper.getSequenceStrings(sequences);
            for (ITracker tracker : trackers) {
                LOGGER.info(String.format("Tracking %s converted by %s by tracker %s", datasetName, codec.toString(), tracker.getName()));
                for (String sequence : sequencesList) {
                    tracker.run(datasetName, sequence, filenameLength);
                    Files.move(
                            symlinkPath.resolve(NamingHelper.getResultFolderName(sequence)),
                            symlinkPath.resolve(NamingHelper.getResultFolderName(sequence, tracker))
                    );
                }
            }

            SymlinkHelper.deleteSymlink(symlinkPath);
        } catch (IOException e) {
            LOGGER.error(String.format("Problem while tracking %s", datasetName), e);
        }
    }

    /**
     * Find the number of digits used for naming image files
     */
    private int getFilenameLength(Path datasetPath) {
        try (Stream<Path> walk = Files.walk(datasetPath)) {
            Path imageFile = walk
                    .filter(Files::isRegularFile)
                    .filter(x -> x.getFileName().toString().toLowerCase().endsWith(Format.TIFF.getExtension()))
                    .findFirst().orElse(null);
            return imageFile != null ? FilenameUtils.getBaseName(imageFile.getFileName().toString()).length() - 1 : 0;
        } catch (IOException e) {
            LOGGER.error(String.format("Error while getting number of digits for images in '%s'", datasetPath.toString()));
        }
        return 0;
    }

    /**
     * Get number of sequences in dataset
     */
    private int getNumberOfSequences(Path datasetPath) {
        try (Stream<Path> walk = Files.walk(datasetPath, 1)) {
            return (int) walk
                    .skip(1)
                    .filter(Files::isDirectory)
                    .filter(x -> x.getFileName().toString().matches("^0+\\d+$"))
                    .count();
        } catch (IOException e) {
            LOGGER.error(String.format("Error while counting sequences in '%s'", datasetPath.toString()));
        }
        return 0;
    }
}
