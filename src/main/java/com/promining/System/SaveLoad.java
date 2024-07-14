package com.promining.System;

import com.promining.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.promining.ProMining.instance;

public class SaveLoad {
    static Path yamlFile = Path.of(File.separator + "Data.yml");
    public void MakeFile() {
        File targetFolder = new File(instance.getDataFolder().getAbsolutePath() + File.separator + "Data");
        File targetYamlFile = new File(targetFolder.getAbsoluteFile().toString() + yamlFile);

        if(!Files.exists(targetFolder.toPath()) || !Files.exists(targetYamlFile.toPath())) {
            try {
                targetFolder.mkdir();
                Files.createFile(targetYamlFile.toPath());
            } catch(Exception ignored) {

            }
        }

        var file = new File(String.valueOf(targetYamlFile));
        instance.configFile = file;
        instance.configData = YamlConfiguration.loadConfiguration(file);
        LoadToConfig();
    }

    public void SaveToFile() {
        try {
            instance.configData.save(instance.configFile); //configからfileに保存する
        } catch (Exception ignored) {
        }
    }


    final String DATA_PATH = "Data.";
    public void SaveToConfig() {
        var data = instance.configData;
        if(data != null && data.contains("Data")) {
            data.set("Data", null); //初期化s
        }
        int counter = 0;
        for(var blockData : Data.markedBlockList) {
            var path = DATA_PATH + "Block." + counter + ".";
            data.set(path + "loc", blockData.getLocation());
            data.set(path + "mat", blockData.getType().toString());
            counter++;
        }

        for(var breakCounter : Data.breakCounterPlayer.entrySet()) {
            var path = DATA_PATH + "Counter." + breakCounter.getKey();
            data.set(path, breakCounter.getValue());
        }
        SaveToFile();
    }

    public void LoadToConfig() {
        if(!instance.configData.contains(DATA_PATH)) return;
        if(instance.configData.contains(DATA_PATH + "Block")) {
            for(var path : instance.configData.getConfigurationSection(DATA_PATH + "Block").getKeys(false)) {
                var resultPath = DATA_PATH + "Block." + path + ".";
                var loc = instance.configData.getLocation(resultPath + "loc");
                var mate = Material.valueOf(instance.configData.getString(resultPath + "mat"));
                var result = loc.getWorld().getBlockAt(loc);
                result.setType(mate);
                Data.markedBlockList.add(result);
            }
        }
        if(instance.configData.contains(DATA_PATH + "Counter")) {
            for(var path : instance.configData.getConfigurationSection(DATA_PATH + "Counter").getKeys(false)) {
                var resultPath = DATA_PATH + "Counter." + path;
                var count = instance.configData.getLong(resultPath);
                Data.breakCounterPlayer.put(UUID.fromString(path), count);
            }
        }
    }
}


