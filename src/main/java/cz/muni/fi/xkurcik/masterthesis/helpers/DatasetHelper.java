package cz.muni.fi.xkurcik.masterthesis.helpers;

import cz.muni.fi.xkurcik.masterthesis.convert.types.Format;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Helper for getting data about datasets
 *
 * @author Lukáš Kurčík
 */
public class DatasetHelper {
    private static final Logger LOGGER = LogManager.getLogger(DatasetHelper.class);

    private DatasetHelper() {
    }

    /**
     * Find the number of digits used for naming image files
     */
    public static int getFilenameLength(Path datasetPath) {
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
    public static int getNumberOfSequences(Path datasetPath) {
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
