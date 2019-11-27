package cz.muni.fi.xkurcik.masterthesis.convert;

import cz.muni.fi.xkurcik.masterthesis.cli.Argument;
import cz.muni.fi.xkurcik.masterthesis.cli.ICliRunner;
import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import javafx.util.Pair;
import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for running conversion on datasets according to configuration
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class ConvertRunner implements ICliRunner {
    private static final Logger LOGGER = LogManager.getLogger(ConvertRunner.class.getName());
    private static final List<Argument> arguments = new ArrayList<>() {
        {
            add(Argument.DATASETS);
            add(Argument.CONVERTED_DIR);
        }
    };

    private Config config;
    private ConverterProvider converterProvider;
    private List<Pair<Codec, String>> codecs;

    public ConvertRunner() {
    }

    @Override
    public void run(CommandLine commandLine, Runtime runtime, Config config) {
        this.config = config;
        this.converterProvider = new ConverterProvider(runtime, config);
        this.codecs = Codec.listFromConfig(config);

        Path datasets = Paths.get(Argument.DATASETS.get(commandLine));
        Path target = Paths.get(Argument.CONVERTED_DIR.get(commandLine));
        boolean deleteHelpImages = Argument.DELETE_HELP_FILES.has(commandLine);

        LOGGER.info("Running convert");
        start(datasets, target, deleteHelpImages);
    }

    /**
     * Run each converter specified in config on each dataset specified in config
     *
     * @param datsetsDir       Directory with all datasets
     * @param targetDir        Directory for saving converted datasets
     * @param deleteHelpImages If true, after finishing will delete all images that were created from tiff for
     *                         converting with specific codec to final format
     */
    private void start(Path datsetsDir, Path targetDir, boolean deleteHelpImages) {
        IDatasetConverter converter = new DatasetConverter(converterProvider);
        config.datasets.parallelStream().forEach(dataset -> {
            converter.convert(datsetsDir.resolve(dataset), targetDir, codecs, deleteHelpImages);
        });
    }

    @Override
    public List<Argument> getNeededArguments() {
        return arguments;
    }
}
