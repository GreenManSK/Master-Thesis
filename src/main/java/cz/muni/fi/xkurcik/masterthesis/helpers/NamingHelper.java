package cz.muni.fi.xkurcik.masterthesis.helpers;

import cz.muni.fi.xkurcik.masterthesis.convert.converters.IConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper for creating names used by the application
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class NamingHelper {
    private NamingHelper() {
    }

    /**
     * Create name for converted dataset
     *
     * @param datasetDir Directory of the original dataset
     * @param codec      Codec used for conversion
     * @param params     Parameters for conversion
     * @param converter  Converted used
     * @return Unique name for converted dataset
     */
    public static String createDatasetName(Path datasetDir, Codec codec, Object params, IConverter converter) {
        String datasetName = datasetDir.getFileName().toString();
        return createDatasetName(datasetName, codec, params, converter);
    }

    /**
     * Create name for converted dataset
     *
     * @param datasetName Name of the original dataset
     * @param codec       Codec used for conversion
     * @param params      Parameters for conversion
     * @param converter   Converted used
     * @return Unique name for converted dataset
     */
    public static String createDatasetName(String datasetName, Codec codec, Object params, IConverter converter) {
        return String.format("%s_%s_%s", datasetName, codec, params instanceof String ? params : converter.serializeParams(params));
    }

    /**
     * Generate list of n sequence name
     *
     * @param sequences number of needed sequences
     * @return List of sequence names as strings
     */
    public static List<String> getSequenceStrings(int sequences) {
        List<String> result = new ArrayList<>();
        for (int i = 1; i <= sequences; i++) {
            result.add(String.format("%02d", i));
        }
        return result;
    }

    /**
     * Create name for result directory for provided sequence
     *
     * @param sequenceName name of the sequence
     * @return name of the result directory
     */
    public static String getResultFolderName(String sequenceName) {
        return sequenceName + "_RES";
    }

    /**
     * Create name for result directory for provided sequence tracked by specified tracker
     *
     * @param sequenceName name of the sequence
     * @param tracker      tracker
     * @return name of the result directory
     */
    public static String getResultFolderName(String sequenceName, ITracker tracker) {
        return sequenceName + "_RES_" + tracker.getName();
    }
}
