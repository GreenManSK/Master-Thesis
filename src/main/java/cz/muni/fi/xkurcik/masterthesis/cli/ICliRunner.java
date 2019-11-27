package cz.muni.fi.xkurcik.masterthesis.cli;

import cz.muni.fi.xkurcik.masterthesis.config.Config;
import org.apache.commons.cli.CommandLine;

import java.util.List;

/**
 * Able to be run from the commandline with CliMain
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface ICliRunner {

    /**
     * Run from CliMain
     *
     * @param commandLine with all needed arguments
     * @param runtime     runtime
     * @param config      application configuration
     */
    void run(CommandLine commandLine, Runtime runtime, Config config);

    /**
     * Return list of arguments needed by the class to be run
     */
    List<Argument> getNeededArguments();
}
