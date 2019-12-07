package cz.muni.fi.xkurcik.masterthesis.convert.converters;

import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.helpers.TiffConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private ConverterProvider converterProvider;

    public NoneConverter(ConverterProvider converterProvider) {
        this.converterProvider = converterProvider;
    }

    @Override
    public void convert(String source, String target, Void params) throws ConversionException {
        Path targetPath = Paths.get(target);
        if (!Files.exists(targetPath)) {
            TiffConverter.toTiff(Paths.get(source), targetPath, converterProvider);
        }
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
