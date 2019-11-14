package cz.muni.fi.xkurcik.masterthesis.convert.helpers;

import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.ConversionException;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.IConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.helpers.NamingHelper;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper for converting dataset using specific codec
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class CodecConverter<N> {
    private static final Logger LOGGER = LogManager.getLogger(CodecConverter.class);

    private Codec codec;
    private ConverterProvider converterProvider;
    private IConverter<N> converter;
    private N params;

    public CodecConverter(ConverterProvider converterProvider, Codec codec, String params) {
        this.codec = codec;
        this.converterProvider = converterProvider;
        this.converter = converterProvider.getByCodec(codec);
        this.params = this.converter.paramsFromString(params);
    }

    /**
     * Create directory for dataset using convert and add all needed files
     */
    public void convert(Path dataset, Path targetDir, List<Path> filesForConversion, List<Path> groundTruthFolders) {
        try {
            Path codecDatasetPath = createDatasetDirectory(dataset, targetDir);
            copyGroundTruth(groundTruthFolders, codecDatasetPath);
            List<Path> convertedImages = convertWithCodec(codecDatasetPath, filesForConversion);
            convertToTiff(convertedImages);
            deleteFiles(convertedImages);
        } catch (IOException e) {
            LOGGER.error(String.format("Error while converting '%s' using %s", dataset.toString(), codec), e);
        }
    }

    /**
     * Convert list of images to tiff
     */
    private void convertToTiff(List<Path> images) {
        for (Path image : images) {
            try {
                TiffConverter.toTiff(image, converterProvider);
            } catch (ConversionException e) {
                LOGGER.error(String.format("Couldn't convert '%s' to TIFF", image.toString()), e);
            }
        }
    }

    /**
     * Delete list of files
     */
    private void deleteFiles(List<Path> files) {
        for (Path file : files) {
            LOGGER.info(String.format("Deleting '%s'", file.toString()));
            try {
                Files.delete(file);
            } catch (IOException e) {
                LOGGER.error(String.format("Couldn't delete '%s'", file.toString()));
            }
        }
    }

    /**
     * Convert images using codec converter
     */
    private List<Path> convertWithCodec(Path codecDatasetPath, List<Path> filesForConversion) throws IOException {
        List<Path> convertedFiles = new ArrayList<>();
        for (Path file : filesForConversion) {
            Path sourceFile = PathsHelper.changeFileExtension(file, codec.getBaseFormat().getExtension());
            Path convertedFile = codecDatasetPath
                    .resolve(sourceFile.getParent().getFileName())
                    .resolve(PathsHelper.changeFileExtension(file, codec.getTargetFormat().getExtension()).getFileName());

            createDirIfNeeded(convertedFile.getParent());
            try {
                if (!Files.exists(convertedFile)) {
                    converter.convert(sourceFile.toString(), convertedFile.toString(), params);
                }
                convertedFiles.add(convertedFile);
            } catch (ConversionException e) {
                LOGGER.error(String.format("Couldn't convert file '%s' to '%s'", sourceFile.toString(), convertedFile.toString()), e);
            }
        }
        return convertedFiles;
    }

    /**
     * Copy ground truth folders to new dataset directory
     */
    private void copyGroundTruth(List<Path> groundTruthFolders, Path codecDatasetPath) throws IOException {
        for (Path gt : groundTruthFolders) {
            Path newGtDir = codecDatasetPath.resolve(gt.getFileName());
            LOGGER.info(String.format("Copying '%s' to '%s'", gt.toString(), newGtDir.toString()));
            FileUtils.copyDirectory(gt.toFile(), newGtDir.toFile());
        }
    }

    /**
     * Create dataset directory in targetDir with name in format {original dataset name}_{codec}_{serialized parameters for codec}
     */
    private Path createDatasetDirectory(Path dataset, Path targetDir) throws IOException {
        String datasetName = NamingHelper.createDatasetName(dataset, codec, params, converter);
        Path codecDatasetPath = targetDir.resolve(datasetName);
        createDirIfNeeded(codecDatasetPath);
        return codecDatasetPath;
    }

    private void createDirIfNeeded(Path path) throws IOException {
        if (!Files.exists(path)) {
            LOGGER.info(String.format("Creating '%s'", path.toString()));
            Files.createDirectory(path);
        }
    }
}
