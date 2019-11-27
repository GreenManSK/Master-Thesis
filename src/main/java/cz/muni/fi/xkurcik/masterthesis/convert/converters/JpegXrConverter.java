package cz.muni.fi.xkurcik.masterthesis.convert.converters;

/**
 * Converter for JPEG-XR.
 * Converter ppm images to jxr using https://github.com/4creators/jxrlib
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class JpegXrConverter extends AExecConverter<String> {
    public static final String NAME = "JPEG_XR";

    public JpegXrConverter(Runtime runtime, String executable) {
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
    protected String constructCommand(String source, String target, String quality) {
        return String.format("%s -i %s -o %s -q %s", executable, source, target, quality);
    }
}
