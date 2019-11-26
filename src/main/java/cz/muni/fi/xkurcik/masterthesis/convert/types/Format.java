package cz.muni.fi.xkurcik.masterthesis.convert.types;

import java.nio.file.Path;

/**
 * Supported image formats
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public enum Format {
    TIFF(".tif"),
    PPM(".ppm"),
    JPEG(".jpeg"),
    JPEG_XR(".jxr"),
    JPEG_2000(".j2k"),
    FLIF(".flif"),
    PGF(".pgf");

    private final String extension;

    Format(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    /**
     * Check if the file has file extension of the format
     *
     * @param file path to the file
     * @return true if file has same extension as the format
     */
    public boolean isFormat(Path file) {
        return file.toString().endsWith(extension);
    }
}
