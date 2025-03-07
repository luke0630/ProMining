package com.promining.System;

import com.promining.Data.Data;
import com.promining.Data.VIPData;
import com.promining.Data.VillagerData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static com.promining.Data.Data.vipData;
import static com.promining.ProMining.instance;
import static com.promining.VillagerScript.VillagerClass.SpawnVillager;
import static com.promining.VillagerScript.VillagerClass.SpawnVillagerLoad;

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

        //****VILLAGER****//
        var vipVillager = new ArrayList<VillagerData>();
        var noVipVillager = new ArrayList<VillagerData>();
        for(var villager : Data.VillagerData) {
            if(villager.getVip() != null) {
                vipVillager.add(villager);
            } else {
                noVipVillager.add(villager);
            }
        }
        //****************//


        int count = 0;
        int blockCount = 0;
        for(var vip : vipData) {
            String path = DATA_PATH + "VipData." + count + ".";
            data.set(path + "name", vip.getVipName());
            data.set(path + "cost", vip.getNeedYen());
            data.set(path + "icon", vip.getVipIcon().toString());
            data.set(path + "description", vip.getDescription());

            for(var countSet : vip.getCountData().entrySet()) {
                data.set(path + "countData." + countSet.getKey(), countSet.getValue().getVipPeriodTime());
            }

            data.set(path + "period", vip.getPeriodPerMinute());

            if(vip.getSelectorData() != null && vip.getSelectorData().getStart() != null && vip.getSelectorData().getEnd() != null) {
                data.set(path + "selectorData.start", vip.getSelectorData().getStart());
                data.set(path + "selectorData.end", vip.getSelectorData().getEnd());
            }

            int villagerCounter = 0;
            for(var villager : vipVillager) {
                if(villager.getVip().equals(vip)) {
                    var villagerPath = path + "villager." + villagerCounter + ".";
                    data.set(villagerPath + "name", villager.getName());
                    data.set(villagerPath + "loc", villager.getLocation());
                    villagerCounter++;
                }
            }

            for(var block : vip.getBlockList()) {
                data.set(path + "blockList." + blockCount + ".loc", block.getLocation());
                data.set(path + "blockList." + blockCount + ".mat", block.getType().toString());
                blockCount++;
            }
            count++;
        }

        int villagerCounter = 0;
        for(var villager : noVipVillager) {
            var villagerPath = DATA_PATH + "villager." + villagerCounter + ".";
            data.set(villagerPath + "name", villager.getName());
            data.set(villagerPath + "loc", villager.getLocation());
            villagerCounter++;
        }
        SaveToFile();
    }

    public void LoadToConfig() {
        if(!instance.configData.contains(DATA_PATH)) return;
        var data = instance.configData;
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

        if(instance.configData.contains(DATA_PATH + "VipData")) {
            for(var path : instance.configData.getConfigurationSection(DATA_PATH + "VipData").getKeys(false)) {
                var resultPath = DATA_PATH + "VipData." + path + ".";
                var loadVipData = new VIPData();
                loadVipData.setVipName(data.getString(resultPath + "name"));
                loadVipData.setNeedYen(data.getInt(resultPath + "cost"));
                loadVipData.setDescription(data.getString(resultPath + "description"));
                loadVipData.setPeriodPerMinute( data.getInt(resultPath + "period") );
                if(data.contains(resultPath + "icon")) {
                    loadVipData.setVipIcon( Material.valueOf(data.getString(resultPath + "icon")) );
                }


                var start = data.getLocation(resultPath + "selectorData.start");
                var end = data.getLocation(resultPath + "selectorData.end");
                loadVipData.setSelectorData( new Data.SelectorData(start, end) );

                if(data.contains(resultPath + "countData")) {
                    var countMap = new HashMap<UUID, Data.CountData>();
                    for(var uuid : data.getConfigurationSection(resultPath + "countData").getKeys(false)) {
                        var uuidResultPath = resultPath + "countData." + uuid;
                        var vipPeriodTime = data.getLong(uuidResultPath);
                        countMap.put(UUID.fromString(uuid), new Data.CountData(null,  vipPeriodTime  ));
                    }
                    loadVipData.setCountData(countMap);
                }


                if(data.contains(resultPath + "blockList")) {
                    var blockList = new ArrayList<Block>();
                    for(var blockCount : data.getConfigurationSection(resultPath + "blockList").getKeys(false)) {
                        var blockPathResult = resultPath + "blockList." + blockCount + ".";
                        var location = data.getLocation( blockPathResult + "loc" );
                        var material = Material.valueOf(data.getString( blockPathResult + "mat" ));
                        var block = location.getWorld().getBlockAt(location);
                        block.setType(material);
                        blockList.add( block );
                    }
                    loadVipData.setBlockList(blockList);
                }

                vipData.add(loadVipData);


                if(data.contains(resultPath + "villager")) {
                    for(var vipVillagerPath : data.getConfigurationSection(resultPath + "villager").getKeys(false)) {
                        var resultVillagerPath = resultPath + "villager." + vipVillagerPath + ".";
                        var name = data.getString(resultVillagerPath + "name");
                        var loc = data.getLocation(resultVillagerPath + "loc");
                        if(loc != null) {
                            SpawnVillagerLoad(loc, loadVipData, name);
                        }
                    }
                }
            }
        }


        if(instance.configData.contains(DATA_PATH + "villager")) {
            for(var villagerCountPath : data.getConfigurationSection(DATA_PATH + "villager").getKeys(false)) {
                var resultPath = DATA_PATH + "villager." + villagerCountPath + ".";
                var name = data.getString(resultPath + "name");
                var loc = data.getLocation(resultPath + "loc");
                if(loc != null) {
                    SpawnVillagerLoad(loc, null, name);
                }
            }
        }
    }
}


