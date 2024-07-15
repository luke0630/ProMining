package com.promining.Data;


import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class VillagerData {
    public Entity getEntityData() {
        return entityData;
    }

    public VillagerData(Location location, UUID villagerUUID, String name, Entity entityData) {
        this.location = location;
        this.villagerUUID = villagerUUID;
        this.name = name;
        this.entityData = entityData;
    }

    public UUID getVillagerUUID() {
        return villagerUUID;
    }

    public void setVillagerUUID(UUID villagerUUID) {
        this.villagerUUID = villagerUUID;
    }

    private Entity entityData = null;

    private Location location = null;

    private UUID villagerUUID = null;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name = "";
}
