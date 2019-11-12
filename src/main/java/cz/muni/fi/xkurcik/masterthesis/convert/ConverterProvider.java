package cz.muni.fi.xkurcik.masterthesis.convert;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.IConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.ImageMagickConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.JpegConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provider for instances of each converter implementation with correct executable
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class ConverterProvider {

    private static final Logger LOGGER = LogManager.getLogger(ConverterProvider.class.getName());

    private JpegConverter jpegConverter;
    private ImageMagickConverter imageMagickConverter;

    public ConverterProvider() {
    }

    public ConverterProvider(Runtime runtime, Config config) {
        fromConfig(runtime, config);
    }

    private void fromConfig(Runtime runtime, Config config) {
        if (config.codecs.containsKey(JpegConverter.NAME)) {
            jpegConverter = new JpegConverter(runtime, config.codecs.get(JpegConverter.NAME));
        }
        if (config.codecs.containsKey(ImageMagickConverter.NAME)) {
            imageMagickConverter = new ImageMagickConverter(runtime, config.codecs.get(ImageMagickConverter.NAME));
        }
    }

    public JpegConverter getJpegConverter() {
        return jpegConverter;
    }

    public void setJpegConverter(JpegConverter jpegConverter) {
        this.jpegConverter = jpegConverter;
    }

    public ImageMagickConverter getImageMagickConverter() {
        return imageMagickConverter;
    }

    public void setImageMagickConverter(ImageMagickConverter imageMagickConverter) {
        this.imageMagickConverter = imageMagickConverter;
    }

    /**
     * Get converter by codec
     */
    public IConverter getByCodec(Codec codec) {
        switch (codec) {
            case JPEG:
                return jpegConverter;
            default:
                String msg = String.format("Codec %s dose not have converter assigned", codec.toString());
                LOGGER.error(msg);
                throw new UnsupportedOperationException(msg);
        }
    }
}
