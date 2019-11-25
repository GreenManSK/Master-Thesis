package cz.muni.fi.xkurcik.masterthesis.evaluate.export;

import cz.muni.fi.xkurcik.masterthesis.evaluate.EvaluationResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Used for exporting lists of EvaluationResult to disk
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface ResultExporter {
    /**
     * Export result list into the supplied output directory
     *
     * @param results         list of results
     * @param outputDirectory path to directory where output can be exported
     * @throws IOException if any problem with IO operations while exporting
     */
    void export(List<EvaluationResult> results, Path outputDirectory) throws IOException;
}
