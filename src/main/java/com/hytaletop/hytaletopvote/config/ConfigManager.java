package com.hytaletop.hytaletopvote.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Config config;

    private final File configFile;

    public ConfigManager() {
        File folder = new File("mods/h-y-tale-vote");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        configFile = new File(folder, "config.json");
        load();
    }

    private void load() {
        try {
            if (!configFile.exists()) {
                config = new Config();
                save(); 
                return;
            }

            config = GSON.fromJson(new java.io.FileReader(configFile), Config.class);

        } catch (Exception e) {
            e.printStackTrace();
            config = new Config();
        }
    }

    public void save() {
        try (Writer writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Config getConfig() {
        return config;
    }

    public void reload() {
        try (Reader reader = new FileReader(configFile)) {
            Config newConfig = GSON.fromJson(reader, Config.class);
            if (newConfig != null) {
                this.config = newConfig;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
