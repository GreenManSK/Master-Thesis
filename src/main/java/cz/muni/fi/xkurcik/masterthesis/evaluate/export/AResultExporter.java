package cz.muni.fi.xkurcik.masterthesis.evaluate.export;

import cz.muni.fi.xkurcik.masterthesis.evaluate.EvaluationResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstraction for functions needed for most of the exporters
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public abstract class AResultExporter implements ResultExporter {

    /**
     * Separate results for easier exporting to csv files
     *
     * @param results        list of results
     * @param mainNameGetter function for getting name of main group from the EvaluationResult
     * @return Map in format {main group name} => {dataset name} => list of results for this converter and dataset
     */
    protected Map<String, Map<String, List<EvaluationResult>>> parseResults(List<EvaluationResult> results, Function<EvaluationResult, String> mainNameGetter) {
        Map<String, Map<String, List<EvaluationResult>>> mainMap = new HashMap<>();
        for (EvaluationResult result : results) {
            String mainGroup = mainNameGetter.apply(result);
            if (!mainMap.containsKey(mainGroup)) {
                mainMap.put(mainGroup, new HashMap<>());
            }

            Map<String, List<EvaluationResult>> datasetMap = mainMap.get(mainGroup);
            String dataset = result.getDataset();
            if (!datasetMap.containsKey(dataset)) {
                datasetMap.put(dataset, new ArrayList<>());
            }
            datasetMap.get(dataset).add(result);
        }
        return mainMap;
    }

    /**
     * Tries to create output directory if it dose not exists
     */
    protected void createOutputDir(Path outputDirectory) throws IOException {
        if (!Files.exists(outputDirectory)) {
            Files.createDirectories(outputDirectory);
        }
    }
}
