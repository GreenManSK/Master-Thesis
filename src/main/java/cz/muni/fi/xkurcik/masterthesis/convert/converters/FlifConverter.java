package cz.muni.fi.xkurcik.masterthesis.convert.converters;

/**
 * Converter for FLIF.
 * Converter ppm images to flif using https://github.com/FLIF-hub/FLIF
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class FlifConverter extends AExecConverter<Integer> {

    public static final String NAME = "FLIF";

    public FlifConverter(Runtime runtime, String executable) {
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
        return String.format("%s -e -Q%d %s %s", executable, quality, source, target);
    }
}
