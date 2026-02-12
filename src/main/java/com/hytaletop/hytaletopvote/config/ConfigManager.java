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

    private RewardConfig config;

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
                config = new RewardConfig();
                save(); 
                return;
            }

            config = GSON.fromJson(new java.io.FileReader(configFile), RewardConfig.class);

        } catch (Exception e) {
            e.printStackTrace();
            config = new RewardConfig();
        }
    }

    public void save() {
        try (Writer writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RewardConfig getConfig() {
        return config;
    }

    public void reload() {
        try (Reader reader = new FileReader(configFile)) {
            RewardConfig newConfig = GSON.fromJson(reader, RewardConfig.class);
            if (newConfig != null) {
                this.config = newConfig;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
