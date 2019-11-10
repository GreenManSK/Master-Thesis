package cz.muni.fi.xkurcik.masterthesis.convert.converters;

import java.io.OutputStream;

/**
 * Implementation of IConvertor are used for converting image files
 *
 * @param <N> Object for storing parameters needed for conversion
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface IConverter<N> {
    /**
     * Converts file to the new format
     *
     * @param source Source file
     * @param target Target file
     * @param params Conversion parameters
     * @throws ConversionException if any problem occurred
     */
    void convert(String source, String target, N params) throws ConversionException;

    /**
     * Converts file and return the result as output stream
     *
     * @param source Source file
     * @param params Conversion parameters
     * @throws ConversionException if any problem occurred
     */
    OutputStream convert(String source, N params) throws ConversionException;

    /**
     * Serialize params to string for using in file names
     *
     * @param params Conversion parameters
     * @return String legal for file name
     */
    String serializeParams(N params);
}
