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
    private IrfanViewConverter irfanViewConverter;
    private JpegConverter jpegConverter;
    private JpegXrConverter jpegXrConverter;
    private OpenJpegConverter openJpegConverter;
    private NoneConverter noneConverter;
    private PgfConverter pgfConverter;

    public ConverterProvider() {
    }

    public ConverterProvider(Runtime runtime, Config config) {
        fromConfig(runtime, config);
    }

    private void fromConfig(Runtime runtime, Config config) {
        noneConverter = new NoneConverter(this);
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
        if (config.codecs.containsKey(IrfanViewConverter.NAME)) {
            irfanViewConverter = new IrfanViewConverter(runtime, config.codecs.get(IrfanViewConverter.NAME));
        }
        if (config.codecs.containsKey(OpenJpegConverter.NAME)) {
            openJpegConverter = new OpenJpegConverter(runtime, config.codecs.get(OpenJpegConverter.NAME));
        }
        if (config.codecs.containsKey(PgfConverter.NAME)) {
            pgfConverter = new PgfConverter(runtime, config.codecs.get(PgfConverter.NAME));
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

    public OpenJpegConverter getOpenJpegConverter() {
        return openJpegConverter;
    }

    public void setOpenJpegConverter(OpenJpegConverter openJpegConverter) {
        this.openJpegConverter = openJpegConverter;
    }

    public PgfConverter getPgfConverter() {
        return pgfConverter;
    }

    public void setPgfConverter(PgfConverter pgfConverter) {
        this.pgfConverter = pgfConverter;
    }

    public ImageMagickConverter getImageMagickConverter() {
        return imageMagickConverter;
    }

    public void setImageMagickConverter(ImageMagickConverter imageMagickConverter) {
        this.imageMagickConverter = imageMagickConverter;
    }

    public IrfanViewConverter getIrfanViewConverter() {
        return irfanViewConverter;
    }

    public void setIrfanViewConverter(IrfanViewConverter irfanViewConverter) {
        this.irfanViewConverter = irfanViewConverter;
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
            case OPEN_JPEG:
                return openJpegConverter;
            case NONE:
                return noneConverter;
            case PGF:
                return pgfConverter;
            default:
                String msg = String.format("Codec %s dose not have converter assigned", codec.toString());
                LOGGER.error(msg);
                throw new UnsupportedOperationException(msg);
        }
    }
}
