package cz.muni.fi.xkurcik.masterthesis.convert.converters;

/**
 * Converter for JPEG 2000
 * Converter ppm images to j2k using OpenJPEG
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class OpenJpegConverter extends AExecConverter<Integer> {

    public static final String NAME = "OPEN_JPEG";

    public OpenJpegConverter(Runtime runtime, String executable) {
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
    protected String constructCommand(String source, String target, Integer quality) {
        return String.format("%s -i %s -o %s -q %d", executable, source, target, quality);
    }
}
