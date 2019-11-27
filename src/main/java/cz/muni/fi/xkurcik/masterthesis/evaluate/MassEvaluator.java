package cz.muni.fi.xkurcik.masterthesis.evaluate;

import cz.muni.fi.xkurcik.masterthesis.convert.ConverterProvider;
import cz.muni.fi.xkurcik.masterthesis.convert.converters.IConverter;
import cz.muni.fi.xkurcik.masterthesis.convert.types.Codec;
import cz.muni.fi.xkurcik.masterthesis.evaluate.evaluators.EvaluationException;
import cz.muni.fi.xkurcik.masterthesis.evaluate.evaluators.EvaluatorRunner;
import cz.muni.fi.xkurcik.masterthesis.helpers.DatasetHelper;
import cz.muni.fi.xkurcik.masterthesis.helpers.NamingHelper;
import cz.muni.fi.xkurcik.masterthesis.helpers.SymlinkHelper;
import cz.muni.fi.xkurcik.masterthesis.track.trackers.ITracker;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Evaluator for running evaluation on multiple datasests
 *
 * @author Lukáš Kurčík
 */
public class MassEvaluator {
    private static final Logger LOGGER = LogManager.getLogger(MassEvaluator.class);

    private EvaluatorRunner evaluatorRunner;
    private ConverterProvider converterProvider;

    /**
     * Create MassEvaluator
     *
     * @param runtime    for running evaluators
     * @param evaluators path to the directory where all evaluators are stored, should be pointing to folder that
     *                   contains sub-folders for each platform's evaluator
     */
    public MassEvaluator(Runtime runtime, Path evaluators, ConverterProvider converterProvider) {
        this.evaluatorRunner = new EvaluatorRunner(runtime, evaluators);
        this.converterProvider = converterProvider;
    }

    /**
     * Run evaluation on results for provided converted datasets tracked by provided trackers
     *
     * @param datasetsPath      Path to original datasets
     * @param datasets          List of datasets to be evaluated
     * @param convertedDatasets Path to converted datasets
     * @param codecs            List of codecs used for conversion
     * @param trackers          List of trackers used for tracking
     * @return List of results
     */
    public List<EvaluationResult> evaluate(
            Path datasetsPath,
            List<String> datasets,
            Path convertedDatasets,
            List<Pair<Codec, String>> codecs,
            List<ITracker> trackers
    ) {
        LOGGER.info(String.format("Starting mass evaluation for results in %s", convertedDatasets.toString()));
        List<EvaluationResult> results = new ArrayList<>();

        for (String dataset : datasets) {
            results.addAll(evaluateDataset(datasetsPath, dataset, convertedDatasets, codecs, trackers));
        }

        return results;
    }

    /**
     * Evaluates dataset and saves its result
     */
    private List<EvaluationResult> evaluateDataset(
            Path datasetsPath,
            String datasetName,
            Path convertedDatasets,
            List<Pair<Codec, String>> codecs,
            List<ITracker> trackers) {
        List<EvaluationResult> results = Collections.synchronizedList(new ArrayList<>());
        Path datasetPath = datasetsPath.resolve(datasetName);

        int sequences = DatasetHelper.getNumberOfSequences(datasetPath);
        if (sequences == 0) {
            LOGGER.warn(String.format("No sequence for '%s' exists", datasetName));
            return results;
        }

        int filenameLength = DatasetHelper.getFilenameLength(datasetPath);
        if (filenameLength == 0) {
            LOGGER.warn(String.format("Invalid filenames in '%s'", datasetName));
            return results;
        }

        List<String> sequencesList = NamingHelper.getSequenceStrings(sequences);
        LOGGER.info(String.format("Evaluating results for dataset %s", datasetName));

        codecs.parallelStream().forEach((codec) -> {
            List<EvaluationResult> partialResult = new ArrayList<>();
            for (ITracker tracker : trackers) {
                for (String sequence : sequencesList) {
                    EvaluationResult result = evaluateSequence(datasetName, convertedDatasets, filenameLength, codec, tracker, sequence);
                    if (result != null) {
                        partialResult.add(result);
                    }
                }
            }
            results.addAll(partialResult);
        });

        return results;
    }

    /**
     * Evaluate concrete sequence of dataset
     */
    private EvaluationResult evaluateSequence(String datasetName, Path convertedDatasets, int filenameLength, Pair<Codec, String> codec, ITracker tracker, String sequence) {
        try {
            EvaluationResult result = new EvaluationResult(datasetName, codec, tracker, sequence);
            IConverter converter = converterProvider.getByCodec(codec.getKey());
            String convertedDatasetName = NamingHelper.createDatasetName(datasetName, codec.getKey(), codec.getValue(), converter);

            Path convertedDatasetPath = convertedDatasets.resolve(convertedDatasetName);
            Path symlinkResultPath = convertedDatasetPath.resolve(NamingHelper.getResultFolderName(sequence));
            Path trackerResultPath = convertedDatasetPath.resolve(NamingHelper.getResultFolderName(sequence, tracker));

            SymlinkHelper.createSymlink(symlinkResultPath, trackerResultPath);
            result.setAll(evaluatorRunner.evaluate(convertedDatasetPath, sequence, filenameLength));
            SymlinkHelper.deleteSymlink(symlinkResultPath);

            return result;
        } catch (EvaluationException | IOException e) {
            LOGGER.error(
                    String.format("Error while evaluating dataset %s, codec %s with %s, tracker %s, sequence %s", datasetName, codec.getKey(), codec.getValue(), tracker.getName(), sequence),
                    e
            );
        }
        return null;
    }
}
