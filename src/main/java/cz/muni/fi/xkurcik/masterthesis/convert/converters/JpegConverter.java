package cz.muni.fi.xkurcik.masterthesis.convert.converters;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Converter using ppm images to jpeg using https://github.com/thorfdbg/libjpeg
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class JpegConverter implements IConverter<Integer> {

    private static final Logger LOGGER = LogManager.getLogger(JpegConverter.class.getName());

    private final Runtime runtime;
    private final String executable;

    /**
     * Creates JpegConverter
     *
     * @param runtime    for executing conversion
     * @param executable path to the executable of codec implementation
     */
    public JpegConverter(Runtime runtime, String executable) {
        this.executable = executable;
        this.runtime = runtime;
    }

    @Override
    public void convert(String source, String target, Integer params) throws ConversionException {
        String command = constructCommand(source, target, params);
        LOGGER.debug(command);
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

    private void checkErrors(String source, Process process) throws ConversionException, IOException {
        if (process.exitValue() != 0) {
            throw new ConversionException(String.format("Unknow problem while converting %s", source));
        }
        try (InputStream is = process.getErrorStream()) {
            String errors = IOUtils.toString(is, Charset.defaultCharset());
            if (Strings.isNullOrEmpty(errors)) {
                throw new ConversionException(String.format("Error while converting %s: %s", source, errors));
            }
        }
    }

    private String constructCommand(String source, String target, Integer quality) throws ConversionException {
        if (quality > 100 || quality < 0) {
            throw new ConversionException("Quality parameter must be in range 1 to 100");
        }
        return String.format("%s -q %d %s %s", executable, quality, source, target);
    }

    private Process execute(String command) throws IOException, InterruptedException {
        Process process = runtime.exec(command);
        process.waitFor();
        return process;
    }
}
