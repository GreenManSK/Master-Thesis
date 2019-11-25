package cz.muni.fi.xkurcik.masterthesis.convert.converters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Only copy original image to new location
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class NoneConverter implements IConverter<Void> {
    private static final Logger LOGGER = LogManager.getLogger(NoneConverter.class.getName());

    public static final String NAME = "NONE";

    public NoneConverter() {
    }

    @Override
    public void convert(String source, String target, Void params) throws ConversionException {
        try {
            Path targetPath = Paths.get(target);
            if (!Files.exists(targetPath)) {
                Files.copy(Paths.get(source), targetPath);
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Error while copying '%s' to '%s'", source, target), e);
        }
    }

    @Override
    public OutputStream convert(String source, Void params) throws ConversionException {
        throw new UnsupportedOperationException("Dose not support conversion to stream");
    }

    @Override
    public String serializeParams(Void params) {
        return "";
    }

    @Override
    public Void paramsFromString(String params) {
        return null;
    }
}
