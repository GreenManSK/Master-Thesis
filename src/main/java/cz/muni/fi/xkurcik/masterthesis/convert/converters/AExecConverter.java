package cz.muni.fi.xkurcik.masterthesis.convert.converters;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Converter that uses Runtime and exec to convert images
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public abstract class AExecConverter<N> implements IConverter<N> {

    private final Logger LOGGER;

    private final Runtime runtime;
    protected final String executable;

    public AExecConverter(Runtime runtime, String executable) {
        LOGGER = LogManager.getLogger(this.getClass().getName());
        this.runtime = runtime;
        this.executable = executable;
    }

    @Override
    public void convert(String source, String target, N params) throws ConversionException {
        String command = constructCommand(source, target, params);
        try {
            Process process = execute(command);
            checkErrors(source, process);
        } catch (IOException | InterruptedException e) {
            LOGGER.error(String.format("Error while converting %s", source), e);
        }
    }

    /**
     * Create command for running converter
     *
     * @param source Source file path
     * @param target Target file path
     * @param params Parameters for converter
     * @return Command that should be run for conversion
     * @throws ConversionException if parameters are not valid
     */
    protected abstract String constructCommand(String source, String target, N params) throws ConversionException;

    protected Process execute(String command) throws IOException, InterruptedException {
        LOGGER.debug(String.format("Executing '%s'", command));
        Process process = runtime.exec(command);
        process.waitFor();
        return process;
    }

    protected void checkErrors(String source, Process process) throws ConversionException, IOException {
        if (process.exitValue() != 0) {
            LOGGER.error(String.format("Process error: %s", getErrorOutput(process)));
            throw new ConversionException(String.format("Unknow problem while converting %s, exit code %d", source, process.exitValue()));
        }
        String errors = getErrorOutput(process);
        if (!Strings.isNullOrEmpty(errors)) {
            throw new ConversionException(String.format("Error while converting %s: %s", source, errors));
        }
    }

    private String getErrorOutput(Process process) throws IOException {
        try (InputStream is = process.getErrorStream()) {
            String errors = IOUtils.toString(is, Charset.defaultCharset());
            return errors == null ? "" : errors.trim();
        }
    }
}
