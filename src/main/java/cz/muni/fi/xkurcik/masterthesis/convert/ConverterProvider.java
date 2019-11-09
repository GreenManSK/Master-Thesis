package cz.muni.fi.xkurcik.masterthesis.convert;

import cz.muni.fi.xkurcik.masterthesis.convert.converters.ImageMagickConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.JpegConverter;

/**
 * Provider for instances of each converter implementation with correct executable
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class ConverterProvider {

    private JpegConverter jpegConverter;
    private ImageMagickConverter imageMagickConverter;

    public ConverterProvider() {
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
}
