package cz.muni.fi.xkurcik.masterthesis.track.trackers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Simple implementation of generic tracker using only one executable to run
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class SimpleTracker implements ITracker {

    private static final Logger LOGGER = LogManager.getLogger(SimpleTracker.class.getName());

    private Runtime runtime;
    private String name;
    private String executable;

    public SimpleTracker(Runtime runtime, String name, Path executable) {
        this.runtime = runtime;
        this.name = name;
        this.executable = executable.toString();
    }

    @Override
    public void run(String dataset, String sequence, int digits) {
        LOGGER.info(String.format("Running %s on %s - %s", name, dataset, sequence));
        String command = createCommand(dataset, sequence, digits);
        try {
            Process process = runtime.exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            LOGGER.error(String.format("Error while running %s on %s - %s", name, dataset, sequence), e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Create command for executing the tracker
     */
    private String createCommand(String dataset, String sequence, int digits) {
        return String.format("%s %s %s %d", executable.toString(), dataset, sequence, digits);
    }
}
