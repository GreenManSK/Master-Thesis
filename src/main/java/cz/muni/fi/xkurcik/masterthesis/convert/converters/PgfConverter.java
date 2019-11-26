package cz.muni.fi.xkurcik.masterthesis.convert.converters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 * @TODO: Comment
 */
public class PgfConverter extends AExecConverter<PgfConverter.Config> {

    private static final Logger LOGGER = LogManager.getLogger(PgfConverter.class.getName());

    public static final String NAME = "PGF";

    public PgfConverter(Runtime runtime, String executable) {
        super(runtime, executable);
    }

    @Override
    public void convert(String source, String target, Config params) throws ConversionException {
        String command = constructCommand(source, target, params);
        try {
            Process process = execute(command);
            checkErrors(source, process);
        } catch (IOException | InterruptedException e) {
            LOGGER.error(String.format("Error while converting %s", source), e);
        }
    }

    @Override
    public OutputStream convert(String source, Config params) throws ConversionException {
        throw new UnsupportedOperationException("Dose not support conversion to stream");
    }

    @Override
    public String serializeParams(Config params) {
        return String.valueOf(params.quality);
    }

    @Override
    public Config paramsFromString(String params) {
        return new Config(false, Integer.valueOf(params));
    }

    private String constructCommand(String source, String target, Config params) throws ConversionException {
        // TODO: Quality check
        if (params.decode) {
            return String.format("%s -d %s %s", executable, source, target);
        }
        return String.format("%s -e -q %d %s %s", executable, params.quality, source, target);
    }

    public static class Config {
        private boolean decode = false;
        private int quality;

        public Config(boolean decode, int quality) {
            this.decode = decode;
            this.quality = quality;
        }

        public boolean isDecode() {
            return decode;
        }

        public int getQuality() {
            return quality;
        }
    }
}
