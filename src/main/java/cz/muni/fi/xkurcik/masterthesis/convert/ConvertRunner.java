package cz.muni.fi.xkurcik.masterthesis.convert;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import javafx.util.Pair;

import java.nio.file.Path;
import java.util.List;

/**
 * Class for running conversion on datasets according to configuration
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class ConvertRunner {

    private Config config;
    private ConverterProvider converterProvider;
    private List<Pair<Codec, String>> codecs;

    public ConvertRunner(Runtime runtime, Config config) {
        this.config = config;
        this.converterProvider = new ConverterProvider(runtime, config);
        this.codecs = Codec.listFromConfig(config);
    }

    /**
     * Run each converter specified in config on each dataset specified in config
     *
     * @param datsetsDir       Directory with all datasets
     * @param targetDir        Directory for saving converted datasets
     * @param deleteHelpImages If true, after finishing will delete all images that were created from tiff for
     *                         converting with specific codec to final format
     */
    public void run(Path datsetsDir, Path targetDir, boolean deleteHelpImages) {
        IDatasetConverter converter = new DatasetConverter(converterProvider);
        config.datasets.parallelStream().forEach(dataset -> {
            converter.convert(datsetsDir.resolve(dataset), targetDir, codecs, deleteHelpImages);
        });
    }
}
