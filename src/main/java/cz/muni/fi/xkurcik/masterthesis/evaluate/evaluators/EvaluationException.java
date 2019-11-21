package cz.muni.fi.xkurcik.masterthesis.evaluate.evaluators;

/**
 * Exception throw when there is problem with running evaluators
 *
 * @author Lukáš Kurčík
 */
public class EvaluationException extends Exception {

    public EvaluationException() {
    }

    public EvaluationException(String message) {
        super(message);
    }

    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
