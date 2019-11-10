package cz.muni.fi.xkurcik.masterthesis.convert.converters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Convert images using ImageMagick https://imagemagick.org/index.php
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class ImageMagickConverter extends AExecConverter<String> {

    private static final Logger LOGGER = LogManager.getLogger(ImageMagickConverter.class.getName());

    public ImageMagickConverter(Runtime runtime, String executable) {
        super(runtime, executable);
    }

    @Override
    public void convert(String source, String target, String params) throws ConversionException {
        String command = constructCommand(source, target, params);
        try {
            Process process = execute(command);
            checkErrors(source, process);
        } catch (IOException | InterruptedException e) {
            LOGGER.error(String.format("Error while converting %s", source), e);
        }
    }

    @Override
    public OutputStream convert(String source, String params) throws ConversionException {
        throw new UnsupportedOperationException("Dose not support conversion to stream");
    }

    @Override
    public String serializeParams(String params) {
        return params;
    }

    private String constructCommand(String source, String target, String params) {
        return String.format("%s convert %s %s %s", executable, params, source, target);
    }
}
