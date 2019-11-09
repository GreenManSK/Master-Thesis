package cz.muni.fi.xkurcik.masterthesis.convert;

import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import javafx.util.Pair;

import java.nio.file.Path;
import java.util.List;

/**
 * Converts datasets from http://celltrackingchallenge.net/ using implemented image converters
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface IDatasetConverter {

    /**
     * For each codecs creates folder in format {original dataset name}_{codec}_{serialized parameters for codec}.
     * Copies GT folders and converts all images needed for analysis with the codec. After that it saves converted
     * images as uncompressed TIFF files
     *
     * @param dataset          Path to dataset folder
     * @param targetDir        Path to directory where folders for converted datasets will be created
     * @param codecs           List of codecs with parameters for conversion
     * @param deleteHelpImages If true, after finishing will delete all images that were created from tiff for
     *                         converting with specific codec to final format
     */
    void convert(Path dataset, Path targetDir, List<Pair<Codec, ?>> codecs, boolean deleteHelpImages);
}
