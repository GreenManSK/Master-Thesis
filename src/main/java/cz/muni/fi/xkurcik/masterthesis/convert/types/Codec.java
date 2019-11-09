package cz.muni.fi.xkurcik.masterthesis.convert.types;

import cz.muni.fi.xkurcik.masterthesis.convert.converters.IConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.JpegConverter;

/**
 * Supported codecs
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public enum Codec {
    JPEG(Format.PPM, JpegConverter.class);

    private Format baseFormat;
    private Class<? extends IConverter> converterClass;

    Codec(Format baseFormat, Class<? extends IConverter> converterClass) {
        this.baseFormat = baseFormat;
        this.converterClass = converterClass;
    }

    /**
     * Return image format that is used as input for the codec
     */
    public Format getBaseFormat() {
        return baseFormat;
    }

    /**
     * Return class used for converting using this codec
     */
    public Class<? extends IConverter> getConverterClass() {
        return converterClass;
    }
}
