package cz.muni.fi.xkurcik.masterthesis.cli;

import org.apache.commons.cli.CommandLine;

/**
 * Arguments used for running commandline
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public enum Argument {
    CONFIG("c", "config", true, "Path to config file, will use config.json if not present"),
    CONVERT("k", "convert", false, "Run convert, needs dataset directory and target director"),
    CONVERTED_DIR("t", "target", true, "Path to directory where converted datasets will be saved"),
    DATASETS("d", "datasets", true, "Path to directory with datasets"),
    DELETE_HELP_FILES("dh", "deleteHelpFiles", false, "Delete help files"),
    HELP("h", "help", false, "Print help for using this application"),
    EVALUATE("e", "evaluate", false, "Run evaluators, needs dataset directory, target directory, evaluators directory and result directory"),
    EVALUATORS("x", "evaluators", true, "Path to directory where evaluators are"),
    RESULTS("r", "results", true, "Path to directory where results will be saved"),
    TRACK("w", "track", false, "Run tracker, needs dataset directory, target directory and trackers directory"),
    TRACKERS_DIR("o", "trackers", true, "Path to directory where trackers are");

    private String shortName;
    private String longName;
    private boolean hasValue;
    private String description;

    Argument(String shortName, String longName, boolean hasValue, String description) {
        this.shortName = shortName;
        this.longName = longName;
        this.hasValue = hasValue;
        this.description = description;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public boolean hasValue() {
        return hasValue;
    }

    public String getDescription() {
        return description;
    }

    public boolean has(CommandLine commandLine) {
        return commandLine.hasOption(shortName);
    }

    public String get(CommandLine commandLine) {
        return commandLine.getOptionValue(shortName);
    }
    public String get(CommandLine commandLine, String defaultValue) {
        return commandLine.getOptionValue(shortName, defaultValue);
    }
}
