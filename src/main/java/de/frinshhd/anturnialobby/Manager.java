package de.frinshhd.anturnialobby;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.frinshhd.anturnialobby.model.Config;
import org.bukkit.command.CommandExecutor;

import java.awt.geom.QuadCurve2D;
import java.io.FileInputStream;
import java.io.IOException;

public class Manager {
    public static Config config;

    public Manager() {

    }

    public static void load() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            config = mapper.readValue(Manager.class.getClassLoader().getResourceAsStream("config.yml"), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
