package cz.muni.fi.xkurcik.masterthesis.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration for the application
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class Config {
    public List<String> datasets = new ArrayList<>();
    public Map<String, String> codecs = new HashMap<>();
    public List<ConverterConfig> converters;
}
