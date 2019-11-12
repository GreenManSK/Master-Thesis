package cz.muni.fi.xkurcik.masterthesis.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Helper for getting Config object from json file
 *
 * @author Lukáš Kurčík <lukas.kurcik@gmail.com>
 */
public class JsonConfig {
    private JsonConfig() {
    }

    public static Config get(Path configPath) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try (FileReader reader = new FileReader(configPath.toString())) {
            return gson.fromJson(reader, Config.class);
        } catch (FileNotFoundException e) {
            throw new IOException("Config file not found", e);
        }
    }
}
