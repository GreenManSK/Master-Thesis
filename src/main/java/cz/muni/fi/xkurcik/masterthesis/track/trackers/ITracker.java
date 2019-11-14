package cz.muni.fi.xkurcik.masterthesis.track.trackers;

/**
 * Representation of tracker from http://celltrackingchallenge.net/
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public interface ITracker {
    /**
     * Run tracker of dataset with provided attributes
     *
     * @param dataset  name
     * @param sequence sequence name
     * @param digits   number of digits in image names
     */
    void run(String dataset, String sequence, int digits);

    /**
     * Get name of the tracker
     */
    String getName();
}
