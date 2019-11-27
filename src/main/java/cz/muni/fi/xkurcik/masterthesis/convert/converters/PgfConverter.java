package cz.muni.fi.xkurcik.masterthesis.convert.converters;

/**
 * Converter for PGF.
 * Converts ppm image to pgf using libPGF
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class PgfConverter extends AExecConverter<PgfConverter.Config> {

    public static final String NAME = "PGF";

    public PgfConverter(Runtime runtime, String executable) {
        super(runtime, executable);
    }

    @Override
    public String serializeParams(Config params) {
        return String.valueOf(params.quality);
    }

    @Override
    public Config paramsFromString(String params) {
        return new Config(false, Integer.parseInt(params));
    }

    @Override
    protected String constructCommand(String source, String target, Config params) throws ConversionException {
        if (params.getQuality() > 30 || params.getQuality() < 1) {
            throw new ConversionException("Quality parameter must be in range 1 to 30");
        }
        if (params.isDecode()) {
            return String.format("%s -d %s %s", executable, source, target);
        }
        return String.format("%s -e -q %d %s %s", executable, params.getQuality(), source, target);
    }

    public static class Config {
        private boolean decode = false;
        private int quality;

        public Config(boolean decode, int quality) {
            this.decode = decode;
            this.quality = quality;
        }

        public boolean isDecode() {
            return decode;
        }

        public int getQuality() {
            return quality;
        }
    }
}
