package cz.muni.fi.xkurcik.masterthesis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class Main
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        System.out.println("Hello Masters!");
        LOGGER.info("Hello Info!");
        LOGGER.error("Hello Error!");
    }
}
