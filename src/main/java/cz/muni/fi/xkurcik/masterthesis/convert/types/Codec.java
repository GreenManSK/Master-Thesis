package cz.muni.fi.xkurcik.masterthesis.convert.types;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.config.ConverterConfig;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.IConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.JpegConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.NoneConverter;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Supported codecs
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public enum Codec {
    JPEG(Format.PPM, Format.JPEG, JpegConverter.class),
    NONE(Format.TIFF, Format.TIFF, NoneConverter.class);

    private Format baseFormat;
    private Format targetFormat;
    private Class<? extends IConverter> converterClass;

    Codec(Format baseFormat, Format targetFormat, Class<? extends IConverter> converterClass) {
        this.baseFormat = baseFormat;
        this.targetFormat = targetFormat;
        this.converterClass = converterClass;
    }

    /**
     * Return image format that is used as input for the codec
     */
    public Format getBaseFormat() {
        return baseFormat;
    }

    /**
     * Return image format that is created using this codec
     */
    public Format getTargetFormat() {
        return targetFormat;
    }

    /**
     * Return class used for converting using this codec
     */
    public Class<? extends IConverter> getConverterClass() {
        return converterClass;
    }

    public static List<Pair<Codec, String>> listFromConfig(Config config) {
        List<Pair<Codec, String>> list = new ArrayList<>();

        for (ConverterConfig converter : config.converters) {
            Codec codec = Codec.valueOf(converter.name);
            list.add(new Pair<>(codec, converter.params));
        }

        return list;
    }
}
