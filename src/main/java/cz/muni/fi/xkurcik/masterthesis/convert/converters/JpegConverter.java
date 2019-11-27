package cz.muni.fi.xkurcik.masterthesis.convert.converters;

/**
 * Converter for JPEG.
 * Converter ppm images to jpeg using https://github.com/thorfdbg/libjpeg
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class JpegConverter extends AExecConverter<Integer> {

    public static final String NAME = "JPEG";

    public JpegConverter(Runtime runtime, String executable) {
        super(runtime, executable);
    }

    @Override
    public String serializeParams(Integer params) {
        return params.toString();
    }

    @Override
    public Integer paramsFromString(String params) {
        return Integer.valueOf(params);
    }

    @Override
    protected String constructCommand(String source, String target, Integer quality) throws ConversionException {
        if (quality > 100 || quality < 0) {
            throw new ConversionException("Quality parameter must be in range 1 to 100");
        }
        return String.format("%s -q %d %s %s", executable, quality, source, target);
    }
}
