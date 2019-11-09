package cz.muni.fi.xkurcik.masterthesis.convert.converters;

/**
 * Exception thrown if there was any problem while converting file
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class ConversionException extends Exception {
    public ConversionException() {
    }

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
