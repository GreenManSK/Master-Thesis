package cz.muni.fi.xkurcik.masterthesis.convert;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.*;
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

    private FlifConverter flifConverter;
    private ImageMagickConverter imageMagickConverter;
    private JpegConverter jpegConverter;
    private JpegXrConverter jpegXrConverter;
    private NoneConverter noneConverter;

    public ConverterProvider() {
    }

    public ConverterProvider(Runtime runtime, Config config) {
        fromConfig(runtime, config);
    }

    private void fromConfig(Runtime runtime, Config config) {
        noneConverter = new NoneConverter();
        if (config.codecs.containsKey(FlifConverter.NAME)) {
            flifConverter = new FlifConverter(runtime, config.codecs.get(FlifConverter.NAME));
        }
        if (config.codecs.containsKey(JpegConverter.NAME)) {
            jpegConverter = new JpegConverter(runtime, config.codecs.get(JpegConverter.NAME));
        }
        if (config.codecs.containsKey(JpegXrConverter.NAME)) {
            jpegXrConverter = new JpegXrConverter(runtime, config.codecs.get(JpegXrConverter.NAME));
        }
        if (config.codecs.containsKey(ImageMagickConverter.NAME)) {
            imageMagickConverter = new ImageMagickConverter(runtime, config.codecs.get(ImageMagickConverter.NAME));
        }
    }

    public FlifConverter getFlifConverter() {
        return flifConverter;
    }

    public void setFlifConverter(FlifConverter flifConverter) {
        this.flifConverter = flifConverter;
    }

    public JpegConverter getJpegConverter() {
        return jpegConverter;
    }

    public void setJpegConverter(JpegConverter jpegConverter) {
        this.jpegConverter = jpegConverter;
    }

    public JpegXrConverter getJpegXrConverter() {
        return jpegXrConverter;
    }

    public void setJpegXrConverter(JpegXrConverter jpegXrConverter) {
        this.jpegXrConverter = jpegXrConverter;
    }

    public ImageMagickConverter getImageMagickConverter() {
        return imageMagickConverter;
    }

    public void setImageMagickConverter(ImageMagickConverter imageMagickConverter) {
        this.imageMagickConverter = imageMagickConverter;
    }

    public NoneConverter getNoneConverter() {
        return noneConverter;
    }

    public void setNoneConverter(NoneConverter noneConverter) {
        this.noneConverter = noneConverter;
    }

    /**
     * Get converter by codec
     */
    public IConverter getByCodec(Codec codec) {
        switch (codec) {
            case FLIF:
                return flifConverter;
            case JPEG:
                return jpegConverter;
            case JPEG_XR:
                return jpegXrConverter;
            case NONE:
                return noneConverter;
            default:
                String msg = String.format("Codec %s dose not have converter assigned", codec.toString());
                LOGGER.error(msg);
                throw new UnsupportedOperationException(msg);
        }
    }
}
