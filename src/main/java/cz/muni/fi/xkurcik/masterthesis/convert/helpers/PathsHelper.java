package cz.muni.fi.xkurcik.masterthesis.convert.helpers;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;

/**
 * Helpers for working with paths
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class PathsHelper {
    private PathsHelper() {

    }

    /**
     * Change extension of file in the path
     *
     * @param file      Path to file
     * @param extension extension with the dot
     * @return path to file with new extension
     */
    public static Path changeFileExtension(Path file, String extension) {
        Path parent = file.getParent();
        return parent.resolve(FilenameUtils.getBaseName(file.toString()) + extension);
    }
}
