package de.frinshhd.anturnialobby;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.frinshhd.anturnialobby.model.Config;

import java.io.IOException;

public class Manager {
    private static Config config;

    public Manager() {
        init();
    }

    public void init() {
        load();
    }

    public Config getConfig() {
        return config;
    }

    public void load() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            config = mapper.readValue(Manager.class.getClassLoader().getResourceAsStream("config.yml"), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
