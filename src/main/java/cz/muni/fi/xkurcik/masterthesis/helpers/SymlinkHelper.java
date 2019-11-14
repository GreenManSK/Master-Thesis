package cz.muni.fi.xkurcik.masterthesis.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Helper for working with symbolic links
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class SymlinkHelper {
    private SymlinkHelper() {
    }

    /**
     * Creates symbolic link, before creating new link deletes already existing link if needed
     *
     * @param link   Path to the link to be created
     * @param target Path to the existing file
     * @throws IOException If problem occurs
     */
    public static void createSymlink(Path link, Path target) throws IOException {
        deleteSymlink(link);
        Files.createSymbolicLink(link, target);
    }

    /**
     * Deletes symbolic link
     *
     * @param link Path to the link
     * @throws IOException If problem occurs
     */
    public static void deleteSymlink(Path link) throws IOException {
        if (Files.exists(link)) {
            Files.delete(link);
        }
    }
}
