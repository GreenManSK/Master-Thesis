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
 * @TODO: Refactor subclasses and change their javadoc
 */
public abstract class AExecConverter<N> implements IConverter<N> {

    private static final Logger LOGGER = LogManager.getLogger(AExecConverter.class.getName());

    private final Runtime runtime;
    protected final String executable;

    public AExecConverter(Runtime runtime, String executable) {
        this.runtime = runtime;
        this.executable = executable;
    }

    protected Process execute(String command) throws IOException, InterruptedException {
        LOGGER.debug(String.format("Executing '%s'", command));
        Process process = runtime.exec(command);
        process.waitFor();
        return process;
    }

    protected void checkErrors(String source, Process process) throws ConversionException, IOException {
        if (process.exitValue() != 0) {
            // TODO: Log error output
            throw new ConversionException(String.format("Unknow problem while converting %s, exit code %d", source, process.exitValue()));
        }
        try (InputStream is = process.getErrorStream()) {
            String errors = IOUtils.toString(is, Charset.defaultCharset());
            if (!Strings.isNullOrEmpty(errors != null ? errors.trim() : null)) {
                throw new ConversionException(String.format("Error while converting %s: %s", source, errors));
            }
        }
    }
}
