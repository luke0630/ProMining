package com.promining.VillagerScript;


import com.promining.Data.VillagerData;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import static com.promining.Data.Data.VillagerData;
import static com.promining.ProMining.Save;
import static com.promining.Useful.toColor;

public class VillagerClass {
    public static boolean SpawnVillager(Location location) {
        if(VillagerData != null) return false;
        var pos = location;
        var entity = location.getWorld().spawnEntity(pos, EntityType.VILLAGER);
        LivingEntity lvEntity  = (LivingEntity) entity;

        entity.setGravity(false);
        entity.setSilent(true);
        entity.setCustomName(toColor("&c&lVIPショップ"));
        entity.setCustomNameVisible(true);
        ((LivingEntity) entity).setAI(true);
        ((LivingEntity) entity).setCollidable(false);

        VillagerData = new VillagerData(location, entity.getUniqueId(), "&c&lVIPショップ", entity); //村人リストに追加する
        Save();
        return true;
    }

    public static void SpawnVillagerByScript(Location location,String name) {
        var pos = location;
        var entity = location.getWorld().spawnEntity(pos, EntityType.VILLAGER);
        LivingEntity lvEntity  = (LivingEntity) entity;

        entity.setGravity(false);
        entity.setSilent(true);
        entity.setCustomName(toColor(name));
        entity.setCustomNameVisible(true);
        ((LivingEntity) entity).setAI(true);
        ((LivingEntity) entity).setCollidable(false);
        VillagerData = new VillagerData(location, entity.getUniqueId(), name, entity); //村人リストに追加する
    }
}
