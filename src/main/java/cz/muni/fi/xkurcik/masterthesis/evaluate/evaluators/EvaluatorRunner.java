package cz.muni.fi.xkurcik.masterthesis.evaluate.evaluators;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Runner for evaluators used by celltrackingchallenge.net
 *
 * @author Lukáš Kurčík
 */
public class EvaluatorRunner {

    private static final String LINUX_DIR = "Linux";
    private static final String WINDOWS_DIR = "Win";
    private static final String MAC_DIR = "Mac";

    private Runtime runtime;
    private Path evaluators;

    /**
     * Create EvaluatorRunner
     *
     * @param runtime    for running evaluators
     * @param evaluators path to the directory where all evaluators are stored, should be pointing to folder that
     *                   contains sub-folders for each platform's evaluator
     */
    public EvaluatorRunner(Runtime runtime, Path evaluators) {
        this.runtime = runtime;
        this.evaluators = evaluators;
    }

    /**
     * Run each evaluator on result of provided sequence of the dataset
     *
     * @param datasetDir path to the dataset directory
     * @param sequence   sequence that should be evaluated
     * @param digits     number of digits in the name of the image files
     * @return Map with results for each evaluator stored as string for preventing lose of precision
     */
    public Map<Evaluator, String> evaluate(Path datasetDir, String sequence, int digits) throws EvaluationException {
        Map<Evaluator, String> result = new HashMap<>();
        for (Evaluator evaluator : Evaluator.values()) {
            result.put(evaluator, evaluate(evaluator, datasetDir, sequence, digits));
        }
        return result;
    }

    /**
     * Run specific evaluator
     */
    private String evaluate(Evaluator evaluator, Path datasetDir, String sequence, int digits) throws EvaluationException {
        Path executable = getExecutalbePath(evaluator);
        String command = createCommand(executable, datasetDir, sequence, digits);
        Process process;
        try {
            process = runtime.exec(command);
            process.waitFor();
            try (InputStream is = process.getInputStream()) {
                String output = IOUtils.toString(is, Charset.defaultCharset());
                return parseOutput(output);
            }
        } catch (IOException | InterruptedException e) {
            throw new EvaluationException(
                    String.format("Error evaluating '%s' %s %d with evaluator %s", datasetDir.toString(), sequence, digits, evaluator.toString()),
                    e
            );
        }
    }

    /**
     * Parse numeric result form the output of the evaluator
     */
    private String parseOutput(String output) {
        return output.replaceFirst("^.*: ", "");
    }

    /**
     * Return path to platform specific executable
     */
    private Path getExecutalbePath(Evaluator evaluator) {
        if (SystemUtils.IS_OS_WINDOWS) {
            return evaluators.resolve(WINDOWS_DIR).resolve(evaluator.getExecutable() + ".exe");
        } else if (SystemUtils.IS_OS_MAC) {
            return evaluators.resolve(MAC_DIR).resolve(evaluator.getExecutable());
        }
        return evaluators.resolve(LINUX_DIR).resolve(evaluator.getExecutable());
    }

    /**
     * Create command for running evaluator
     */
    private String createCommand(Path executable, Path datasetDir, String sequence, int digits) {
        return String.format("%s \"%s\" %s %d", executable.toString(), datasetDir.toString(), sequence, digits);
    }
}
