package com.promining.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class GUIAbstract {
    public abstract GUIManager.GUI getType();

    @NotNull
    public abstract Inventory getInventory(Player player);

    public abstract void clickAction(InventoryClickEvent event);
}
