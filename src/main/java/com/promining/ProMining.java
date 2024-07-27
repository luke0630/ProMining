package com.promining;

import com.promining.Command.CommandManager;
import com.promining.Data.Data;
import com.promining.Data.VIPData;
import com.promining.Listening.GUIListener;
import com.promining.Listening.Listener;
import com.promining.Listening.SelectorListening;
import com.promining.Placeholder.ProMiningExpansion;
import com.promining.System.SaveLoad;
import com.promining.VillagerScript.VillagerSellerEvents;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class ProMining extends JavaPlugin {
    public static ProMining instance;
    public File configFile;
    public YamlConfiguration configData;
    private static Economy econ = null;
    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Vaultが見つかんなかったからプラグインは無効になります！", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new Listener(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new SelectorListening(), this);
        getServer().getPluginManager().registerEvents(new VillagerSellerEvents(), this);
        Objects.requireNonNull(getServer().getPluginCommand("mining")).setExecutor(new CommandManager());

        instance = this;

        new ProMiningExpansion().register(); //
        new SaveLoad().MakeFile();

        Map<String, Long> breakCounterPlayer = new TreeMap<>(Collections.reverseOrder());

        breakCounterPlayer.put("test", 100L);
        breakCounterPlayer.put("uee", 32L);
        breakCounterPlayer.put("ueeda", 55L);
        for(var data : breakCounterPlayer.values()) {
            getServer().getLogger().info(": " + data);
        }
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(var villager : Data.VillagerData) {
            villager.getEntityData().remove();
        }
    }

    public static void Save() {
        new SaveLoad().SaveToConfig();
    }

    public static Economy getEcon() {
        return econ;
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
