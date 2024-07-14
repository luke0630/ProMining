package com.promining;

import com.promining.Data.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Useful {
    public static String toColor(String value) {
        return ChatColor.translateAlternateColorCodes('&',value);
    }

    public static void setLore(ItemStack item, List<String> lore) {
        var meta = item.getItemMeta();
        var resultLore = new ArrayList<String>();
        for(var l : lore) {
            resultLore.add(toColor(l));
        }
        meta.setLore(resultLore);
        item.setItemMeta(meta);
    }

    public static void setBottomControlOnGUI(Inventory inventory) {
        var size = inventory.getSize();
        int lines = size / 9;
        lines--; //index対応
        for(int i=lines*9;i < size;i++) {
            inventory.setItem(i,Useful.getItem(Material.BLACK_STAINED_GLASS_PANE," "));
        }
    }

    public static ItemStack getItem(Material material, String name) {
        var item = new ItemStack(material);
        var meta = item.getItemMeta();

        meta.setDisplayName(toColor(name));

        item.setItemMeta(meta);
        return item;
    }

    public static Inventory getInitInventory(int size, String title) {
        return Bukkit.createInventory(null, size, toColor(title));
    }
    public static String getTidyLocation(Location location) {
        return "X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ();
    }

    public static Integer getIndexWithSlot(Player player, int currentSlot) {
        if(Data.playerOpenGUI.containsKey(player)) {
            var currentPage = Data.playerOpenPage.get(player);
            currentPage *= 45;
            currentPage += currentSlot;
            return currentPage;
        }
        return currentSlot;
    }
}

