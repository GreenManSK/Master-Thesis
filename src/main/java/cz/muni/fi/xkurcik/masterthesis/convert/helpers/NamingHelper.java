package cz.muni.fi.xkurcik.masterthesis.convert.helpers;

import cz.muni.fi.xkurcik.masterthesis.convert.converters.IConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class NameHelper
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class NamingHelper {
    private NamingHelper() {
    }

    public static String createDatasetName(Path datasetDir, Codec codec, Object params, IConverter converter) {
        String datasetName = datasetDir.getFileName().toString();
        return createDatasetName(datasetName, codec, params, converter);
    }

    public static String createDatasetName(String datasetName, Codec codec, Object params, IConverter converter) {
        return String.format("%s_%s_%s", datasetName, codec, converter.serializeParams(params));
    }

    public static List<String> getSequenceStrings(int sequences) {
        List<String> result = new ArrayList<>();
        for (int i = 1; i <= sequences; i++) {
            result.add(String.format("%02d", i));
        }
        return result;
    }

    public static String getResultFolderName(String sequenceName) {
        return sequenceName + "_RES";
    }

    public static String getResultFolderName(String sequenceName, ITracker tracker) {
        return sequenceName + "_RES_" + tracker.getName();
    }
}
