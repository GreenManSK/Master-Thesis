package cz.muni.fi.xkurcik.masterthesis.evaluate.evaluators;

/**
 * Types of evaluators for results of tracking
 * @author Lukáš Kurčík
 */
public enum Evaluator {
    DET("DETMeasure"), SEG("SEGMeasure"), TRA("TRAMeasure");

    private String executable;

    Evaluator(String executable) {
        this.executable = executable;
    }

    public String getExecutable() {
        return executable;
    }
}
