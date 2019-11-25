package cz.muni.fi.xkurcik.masterthesis.evaluate;

import com.google.common.base.MoreObjects;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.evaluate.evaluators.Evaluator;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for result of evaluation of one sequence of one dataset
 *
 * @author Lukáš Kurčík
 */
public class EvaluationResult {
    private String dataset;
    private Pair<Codec, String> codec;
    private ITracker tracker;
    private String sequence;
    private HashMap<Evaluator, String> results;

    public EvaluationResult(String dataset, Pair<Codec, String> codec, ITracker tracker, String sequence) {
        this.dataset = dataset;
        this.codec = codec;
        this.tracker = tracker;
        this.sequence = sequence;
        results = new HashMap<>();
    }

    public String get(Evaluator evaluator) {
        return results.get(evaluator);
    }

    public void set(Evaluator evaluator, String result) {
        results.put(evaluator, result);
    }

    public void setAll(Map<Evaluator, String> evaluation) {
        results.putAll(evaluation);
    }

    public String getDataset() {
        return dataset;
    }

    public Pair<Codec, String> getCodec() {
        return codec;
    }

    public ITracker getTracker() {
        return tracker;
    }

    public String getSequence() {
        return sequence;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dataset", dataset)
                .add("codec", codec)
                .add("tracker", tracker)
                .add("sequence", sequence)
                .add("results", results)
                .toString();
    }
}
