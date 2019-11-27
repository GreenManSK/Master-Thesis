package cz.muni.fi.xkurcik.masterthesis.cli;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import cz.muni.fi.xkurcik.masterthesis.config.JsonConfig;
import cz.muni.fi.xkurcik.masterthesis.convert.ConvertRunner;
import cz.muni.fi.xkurcik.masterthesis.evaluate.EvaluateRunner;
import cz.muni.fi.xkurcik.masterthesis.track.TrackerRunner;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Takes care of running the application in command line
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class CliMain {
    private static final Logger LOGGER = LogManager.getLogger(CliMain.class.getName());

    private static final String DEFAULT_CONFIG = "config.json";

    private final Map<Argument, ICliRunner> runners;
    private final Options options;

    private CliMain() {
        runners = new LinkedHashMap<>() {
            {
                put(Argument.CONVERT, new ConvertRunner());
                put(Argument.TRACK, new TrackerRunner());
                put(Argument.EVALUATE, new EvaluateRunner());
            }
        };
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
            if (Argument.HELP.has(commandLine) || !shouldRun(commandLine)) {
                printHelp();
                return;
            }

            Config config = JsonConfig.get(Paths.get(Argument.CONFIG.get(commandLine, DEFAULT_CONFIG)));
            runRunners(commandLine, config);
        } catch (ParseException e) {
            System.out.println("Problem while parsing arguments. Try using -h for help");
        } catch (IOException e) {
            LOGGER.error("Couldn't load config file", e);
        }
    }

    /**
     * Check which runners should be run and run them
     */
    private void runRunners(CommandLine commandLine, Config config) {
        for (Map.Entry<Argument, ICliRunner> runnerEntry : runners.entrySet()) {
            if (runnerEntry.getKey().has(commandLine)) {
                if (!runRunner(commandLine, config, runnerEntry.getValue())) {
                    return;
                }
            }
        }
    }

    /**
     * Check arguments for runner and run it
     *
     * @return true if can be run, false otherwise
     */
    private boolean runRunner(CommandLine commandLine, Config config, ICliRunner runner) {
        for (Argument argument : runner.getNeededArguments()) {
            if (!argument.has(commandLine)) {
                LOGGER.error(String.format("For running %s needs argument -%s", runner.getClass().getName(), argument.toString()));
                return false;
            }
        }
        runner.run(commandLine, Runtime.getRuntime(), config);
        return true;
    }

    private boolean shouldRun(CommandLine commandLine) {
        for (Argument argument : runners.keySet()) {
            if (argument.has(commandLine))
                return true;
        }
        return false;
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("mvn exec:java -q -Dexec.args=\"ARGUMENTS\"", options);
    }

    private Options buildOptions() {
        Options options = new Options();
        for (Argument argument : Argument.values()) {
            options.addOption(argument.getShortName(), argument.getLongName(), argument.hasValue(), argument.getDescription());
        }
        return options;
    }
}
