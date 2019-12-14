package cz.muni.fi.xkurcik.masterthesis.convert.converters;

/**
 * Convert images using IrfanView https://www.irfanview.com/
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class IrfanViewConverter extends AExecConverter<String> {

    public static final String NAME = "IRFAN_VIEW";

    public IrfanViewConverter(Runtime runtime, String executable) {
        super(runtime, executable);
    }

    @Override
    protected String constructCommand(String source, String target, String params) throws ConversionException {
        return String.format("%s %s %s /convert=%s", executable, params, source, target);
    }

    @Override
    public String serializeParams(String params) {
        return params;
    }

    @Override
    public String paramsFromString(String params) {
        return params;
    }
}
