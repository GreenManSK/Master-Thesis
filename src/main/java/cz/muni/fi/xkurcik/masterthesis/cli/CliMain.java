package cz.muni.fi.xkurcik.masterthesis.cli;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Takes care of running the application in command line
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
final public class CliMain {
    private static final Logger LOGGER = LogManager.getLogger(CliMain.class.getName());


    private static final String HELP_ARG_SHORT = "h";
    private static final String HELP_ARG_LONG = "help";

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
            printHelp();
        } catch (ParseException e) {
            System.out.println("Problem while parsing arguments. Try using -h for help");
        }
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("mvn exec:java -q -Dexec.args=\"ARGUMENTS\"", options);
    }

    private Options buildOptions() {
        Options options = new Options();

        options.addOption(HELP_ARG_SHORT, HELP_ARG_LONG, false, "Print help for using this application");

        return options;
    }
}
