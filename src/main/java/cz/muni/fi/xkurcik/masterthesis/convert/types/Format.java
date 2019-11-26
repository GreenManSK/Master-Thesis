package cz.muni.fi.xkurcik.masterthesis.convert.types;

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
    FLIF(".flif");

    private final String extension;

    Format(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
