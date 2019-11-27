package cz.muni.fi.xkurcik.masterthesis.convert.converters;

/**
 * Convert images using ImageMagick https://imagemagick.org/index.php
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class ImageMagickConverter extends AExecConverter<String> {

    public static final String NAME = "IMAGE_MAGIC";

    public ImageMagickConverter(Runtime runtime, String executable) {
        super(runtime, executable);
    }

    @Override
    public String serializeParams(String params) {
        return params;
    }

    @Override
    public String paramsFromString(String params) {
        return params;
    }

    @Override
    protected String constructCommand(String source, String target, String params) {
        return String.format("%s convert %s %s %s", executable, params, source, target);
    }
}
