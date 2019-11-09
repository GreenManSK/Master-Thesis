package cz.muni.fi.xkurcik.masterthesis.convert.helpers;

import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.ConversionException;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.ImageMagickConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Format;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for converting from and to .tiff format
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class TiffConverter {
    private static final Logger LOGGER = LogManager.getLogger(TiffConverter.class);

    private static final String COMPRESS_NONE = "-compress none";

    /**
     * Converts .tiff file to specified format and returns path to the new file with correct file extension
     */
    public static Path toFormat(Path source, Format target, ConverterProvider converterProvider) throws ConversionException {
        LOGGER.debug(String.format("Converting '%s' to %s", source.toString(), target.getExtension()));
        ImageMagickConverter converter = converterProvider.getImageMagickConverter();
        String fileName = FilenameUtils.getBaseName(source.toString()) + target.getExtension();
        Path targetPath = source.resolveSibling(Paths.get(fileName));
        if (!Files.exists(targetPath)) {
            converter.convert(source.toString(), targetPath.toString(), COMPRESS_NONE);
        }
        return targetPath;
    }
}
