package cz.muni.fi.xkurcik.masterthesis.convert.helpers;

import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.ConversionException;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.ImageMagickConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.PgfConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Format;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class for converting from and to .tiff format
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class TiffConverter {
    private static final Logger LOGGER = LogManager.getLogger(TiffConverter.class);

    private static final String COMPRESS_NONE = "";//"-compress none";

    /**
     * Converts .tif file to specified format and returns path to the new file with correct file extension
     */
    public static Path toFormat(Path source, Format target, ConverterProvider converterProvider) throws ConversionException {
        LOGGER.debug(String.format("Converting '%s' to %s", source.toString(), target.getExtension()));
        ImageMagickConverter converter = converterProvider.getImageMagickConverter();
        Path targetPath = PathsHelper.changeFileExtension(source, target.getExtension());
        if (!Files.exists(targetPath)) {
            converter.convert(source.toString(), targetPath.toString(), COMPRESS_NONE);
        }
        return targetPath;
    }

    /**
     * Converts image to tif file and returns path to the new file
     */
    public static Path toTiff(Path source, ConverterProvider converterProvider) throws ConversionException {
        Path targetPath = PathsHelper.changeFileExtension(source, Format.TIFF.getExtension());
        LOGGER.debug(String.format("Converting '%s' to %s", source.toString(), Format.TIFF));
        if (!Files.exists(targetPath)) {
            if (Format.PGF.isFormat(source)) {
                PgfConverter converter = converterProvider.getPgfConverter();
                converter.convert(source.toString(), targetPath.toString(), new PgfConverter.Config(true, 0));
            } else {
                ImageMagickConverter converter = converterProvider.getImageMagickConverter();
                converter.convert(source.toString(), targetPath.toString(), COMPRESS_NONE);
            }
        }
        return targetPath;
    }
}
