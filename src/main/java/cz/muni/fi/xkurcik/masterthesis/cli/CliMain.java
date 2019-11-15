package cz.muni.fi.xkurcik.masterthesis.cli;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.config.JsonConfig;
import cz.muni.fi.xkurcik.masterthesis.convert.ConvertRunner;
import cz.muni.fi.xkurcik.masterthesis.track.TrackerRunner;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Takes care of running the application in command line
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class CliMain {
    private static final Logger LOGGER = LogManager.getLogger(CliMain.class.getName());

    private static final String DEFAULT_CONFIG = "config.json";

    private static final String HELP_ARG_SHORT = "h";
    private static final String HELP_ARG_LONG = "help";

    private static final String CONFIG_ARG_SHORT = "c";
    private static final String CONFIG_ARG_LONG = "config";

    private static final String TARGET_ARG_SHORT = "t";
    private static final String TARGET_ARG_LONG = "target";

    private static final String DATASETS_ARG_SHORT = "d";
    private static final String DATASETS_ARG_LONG = "datasets";

    private static final String TRACKERS_ARG_SHORT = "o";
    private static final String TRACKERS_ARG_LONG = "trackersDir";

    private static final String CONVERT_ARG_SHORT = "x";
    private static final String CONVERT_ARG_LONG = "convert";

    private static final String TRACK_ARG_SHORT = "w";
    private static final String TRACK_ARG_LONG = "track";

    private static final String DELETE_HELP_FILES_ARG_SHORT = "dh";
    private static final String DELETE_HELP_FILES_ARG_LONG = "deleteHelpFiles";


    private final Options options;

    private CliMain() {
        options = buildOptions();
    }

    public static void main(String[] args) {
        CliMain cliMain = new CliMain();
        cliMain.run(args);
    }

    /**
     * Parse input arguments for the application and do action based on them.
     * Use -h or --help for showing list of possible arguments
     *
     * @param args array of arguments from main function
     */
    private void run(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption(HELP_ARG_SHORT) || !shouldRun(commandLine)) {
                printHelp();
                return;
            }

            Config config = JsonConfig.get(Paths.get(commandLine.getOptionValue(CONFIG_ARG_SHORT, DEFAULT_CONFIG)));
            if (commandLine.hasOption(CONVERT_ARG_SHORT)) {
                runConvert(commandLine, config);
            }
            if (commandLine.hasOption(TRACK_ARG_SHORT)) {
                runTracker(commandLine, config);
            }
            // @TODO: evaluate
        } catch (ParseException e) {
            System.out.println("Problem while parsing arguments. Try using -h for help");
        } catch (IOException e) {
            LOGGER.error("Couldn't load config file", e);
        }
    }

    private void runConvert(CommandLine commandLine, Config config) {
        if (!commandLine.hasOption(DATASETS_ARG_SHORT)) {
            System.out.println("For running converts needs -" + DATASETS_ARG_SHORT);
            return;
        }
        if (!commandLine.hasOption(TARGET_ARG_SHORT)) {
            System.out.println("For running converts needs -" + TARGET_ARG_SHORT);
            return;
        }
        System.out.println("Running convert");
        Path datasets = Paths.get(commandLine.getOptionValue(DATASETS_ARG_SHORT));
        Path target = Paths.get(commandLine.getOptionValue(TARGET_ARG_SHORT));
        ConvertRunner convertRunner = new ConvertRunner(Runtime.getRuntime(), config);
        convertRunner.run(datasets, target, false);
    }

    private void runTracker(CommandLine commandLine, Config config) {
        if (!commandLine.hasOption(DATASETS_ARG_SHORT)) {
            System.out.println("For running converts needs -" + DATASETS_ARG_SHORT);
            return;
        }
        if (!commandLine.hasOption(TARGET_ARG_SHORT)) {
            System.out.println("For running converts needs -" + TARGET_ARG_SHORT);
            return;
        }
        if (!commandLine.hasOption(TRACKERS_ARG_SHORT)) {
            System.out.println("For running converts needs -" + TRACKERS_ARG_SHORT);
            return;
        }
        System.out.println("Running track");
        Path datasets = Paths.get(commandLine.getOptionValue(DATASETS_ARG_SHORT));
        Path target = Paths.get(commandLine.getOptionValue(TARGET_ARG_SHORT));
        Path trackers = Paths.get(commandLine.getOptionValue(TRACKERS_ARG_SHORT));
        TrackerRunner trackerRunner = new TrackerRunner(Runtime.getRuntime(), config);
        trackerRunner.run(datasets, target, trackers);
    }

    private boolean shouldRun(CommandLine commandLine) {
        return commandLine.hasOption(CONVERT_ARG_SHORT);
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("mvn exec:java -q -Dexec.args=\"ARGUMENTS\"", options);
    }

    private Options buildOptions() {
        Options options = new Options();

        options.addOption(HELP_ARG_SHORT, HELP_ARG_LONG, false, "Print help for using this application");
        options.addOption(CONFIG_ARG_SHORT, CONFIG_ARG_LONG, true, "Path to config file, will use config.json if not present");
        options.addOption(CONVERT_ARG_SHORT, CONVERT_ARG_LONG, false, "Run convert, needs dataset directory and target directory");
        options.addOption(TRACK_ARG_SHORT, TRACK_ARG_LONG, false, "Run tracker, needs dataset directory, target directory and trackers directory");

        options.addOption(DATASETS_ARG_SHORT, DATASETS_ARG_LONG, true, "Path to directory with datasets");
        options.addOption(TARGET_ARG_SHORT, TARGET_ARG_LONG, true, "Path to directory where data will be saved");
        options.addOption(TRACKERS_ARG_SHORT, TRACKERS_ARG_LONG, true, "Path to directory where trackers are");

        options.addOption(DELETE_HELP_FILES_ARG_SHORT, DELETE_HELP_FILES_ARG_LONG, false, "Delete help files");

        return options;
    }
}
