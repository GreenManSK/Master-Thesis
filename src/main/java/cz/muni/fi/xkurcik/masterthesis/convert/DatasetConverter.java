package cz.muni.fi.xkurcik.masterthesis.convert;

import cz.muni.fi.xkurcik.masterthesis.convert.converters.ConversionException;
import cz.muni.fi.xkurcik.masterthesis.convert.helpers.CodecConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.helpers.TiffConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Format;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Basic implementation of IDatasetConverter
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class DatasetConverter implements IDatasetConverter {
    private static final Logger LOGGER = LogManager.getLogger(DatasetConverter.class);

    private static final String TIFF_EXTENSION = ".tif";
    private static final String GROUND_TRUTH_ENDING = "_GT";

    private ConverterProvider converterProvider;

    public DatasetConverter(ConverterProvider converterProvider) {
        this.converterProvider = converterProvider;
    }

    @Override
    public void convert(Path dataset, Path targetDir, List<Pair<Codec, ?>> codecs, boolean deleteHelpImages) {
        // Check if dataset exists
        if (!checkDatasetFolder(dataset)) {
            LOGGER.error(String.format("Invalid dataset folder '%s'", dataset.toString()));
            return;
        }

        Set<Format> helpFormats = createHelpFormatsList(codecs);

        // Get list of all tiffs for conversion
        List<Path> filesForConversion = getFilesForConversion(dataset);
        if (filesForConversion == null) {
            return;
        }

        // Get list of folders with ground truth
        List<Path> groundTruthFolders = getSequenceDirs(dataset, true);
        if (groundTruthFolders == null) {
            return;
        }

        // Create start formats (and return list of them for deletion?)
        List<Path> helpFiles = createHelpFiles(filesForConversion, helpFormats);
        if (helpFiles == null) {
            return;
        }

        // Magic
        createDatasets(dataset, targetDir, codecs, filesForConversion, groundTruthFolders);

        // Delete start formats (thanks list?)
        if (deleteHelpImages) {
            deleteFiles(helpFiles);
        }
    }

    /**
     * Crate converted datasets for each codec
     */
    private void createDatasets(Path dataset, Path targetDir, List<Pair<Codec, ?>> codecs, List<Path> filesForConversion, List<Path> groundTruthFolders) {
        for (Pair<Codec, ?> codecPair : codecs) {
            CodecConverter<Object> converter = new CodecConverter<>(converterProvider, codecPair.getKey(), codecPair.getValue());
            converter.convert(dataset, targetDir, filesForConversion, groundTruthFolders);
        }
    }

    /**
     * Convert data file to all needed formats
     */
    private List<Path> createHelpFiles(List<Path> filesForConversion, Set<Format> helpFormats) {
        List<Path> helpFiles = new ArrayList<>();
        for (Format helpFormat : helpFormats) {
            if (helpFormat == Format.TIFF)
                continue;
            for (Path file : filesForConversion) {
                try {
                    helpFiles.add(TiffConverter.toFormat(file, helpFormat, converterProvider));
                } catch (ConversionException e) {
                    LOGGER.error(String.format("Error while converting file '%s' to %s", file.toString(), helpFormat.getExtension()));
                    return null;
                }
            }
        }
        return helpFiles;
    }

    /**
     * Delete list of files
     */
    private void deleteFiles(List<Path> files) {
        for (Path file : files) {
            try {
                LOGGER.debug(String.format("Deleting '%s'", file.toString()));
                Files.delete(file);
            } catch (IOException e) {
                LOGGER.error(String.format("Couldn't delete file '%s'", file.toString()), e);
            }
        }
    }

    /**
     * Get list of tiff files needed to be converted
     */
    private List<Path> getFilesForConversion(Path datasetPath) {
        List<Path> sequenceDirs = getSequenceDirs(datasetPath, false);
        if (sequenceDirs == null)
            return null;
        List<Path> files = new ArrayList<>();
        for (Path sequenceDir : sequenceDirs) {
            try (Stream<Path> sequenceWalk = Files.walk(sequenceDir)) {
                sequenceWalk
                        .filter(Files::isRegularFile)
                        .filter(x -> x.toString().toLowerCase().endsWith(TIFF_EXTENSION))
                        .collect(Collectors.toCollection(() -> files));
            } catch (IOException e) {
                LOGGER.error(String.format("Error while getting files for conversion from '%s'", datasetPath.toString()));
            }
        }
        return files;
    }

    /**
     * Return list of folders with sequence files
     *
     * @param groundTruth If true return only folders with ground truth, if false only folders with normal data
     */
    private List<Path> getSequenceDirs(Path datasetPath, boolean groundTruth) {
        try (Stream<Path> walk = Files.walk(datasetPath, 1)) {
            return walk
                    .skip(1)
                    .filter(Files::isDirectory)
                    .filter(x -> groundTruth == x.toString().endsWith(GROUND_TRUTH_ENDING))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error(String.format("Error while getting files for conversion from '%s'", datasetPath.toString()));
        }
        return null;
    }

    /**
     * Create list of formats that need to be created for all codecs to be able to convert files
     */
    private Set<Format> createHelpFormatsList(List<Pair<Codec, ?>> codecs) {
        HashSet<Format> set = new HashSet<>();
        for (Pair<Codec, ?> codecPair : codecs) {
            set.add(codecPair.getKey().getBaseFormat());
        }
        return set;
    }

    /**
     * Check if dataset folder is valid
     */
    private boolean checkDatasetFolder(Path dataset) {
        return Files.exists(dataset) && Files.isDirectory(dataset);
    }
}
