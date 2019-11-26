package cz.muni.fi.xkurcik.masterthesis.convert.converters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Converter ppm images to j2k using OpenJPEG
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class OpenJpegConverter extends AExecConverter<Integer> {

    private static final Logger LOGGER = LogManager.getLogger(OpenJpegConverter.class.getName());

    public static final String NAME = "OPEN_JPEG";

    public OpenJpegConverter(Runtime runtime, String executable) {
        super(runtime, executable);
    }

    @Override
    public void convert(String source, String target, Integer params) throws ConversionException {
        String command = constructCommand(source, target, params);
        try {
            Process process = execute(command);
            checkErrors(source, process);
        } catch (IOException | InterruptedException e) {
            LOGGER.error(String.format("Error while converting %s", source), e);
        }
    }

    @Override
    public OutputStream convert(String source, Integer params) throws ConversionException {
        throw new UnsupportedOperationException("Dose not support conversion to stream");
    }

    @Override
    public String serializeParams(Integer params) {
        return params.toString();
    }

    @Override
    public Integer paramsFromString(String params) {
        return Integer.valueOf(params);
    }

    private String constructCommand(String source, String target, Integer quality) throws ConversionException {
        if (quality > 100 || quality < 0) {
            throw new ConversionException("Quality parameter must be in range 1 to 100");
        }
        return String.format("%s -i %s -o %s -q %d", executable, source, target, quality);
    }
}
