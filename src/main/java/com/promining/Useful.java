package com.promining;

import com.promining.Data.Data;
import com.promining.System.RunnableSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.promining.Data.Data.clickedMiningBlock;

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


    public static class TheTime {
        private Integer hour = 0;
        private Integer minute = 0;
        private Integer second = 0;

        public TheTime(Integer hour, Integer minute, Integer second) {
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }
    }

    public static TheTime getTheTimeFromSecond(int second) {
        int h = second / 3600;
        int m = (second % 3600) / 60;
        int s = second % 60;

        return new TheTime(h, m, s);
    }
    public static TheTime getHourFromMinute(int minute) {
        int h = minute / 60;
        int m = minute - 60*h;
        return new TheTime(h, m, 0);
    }
    public static String getTidyTime(TheTime data) {
        return data.hour + "時間:" + data.minute + "分:" + data.second + "秒";
    }


    public static void getClickedBlock(Player player, RunnableSystem.Runnable runnable) {
        clickedMiningBlock.put(player, runnable);
    }


    public static Double isCanPay(Player player, Double needPay) {
        var current = ProMining.getEcon().getBalance(player);
        if(current-needPay < 0) return null;
        return current-needPay;
    }

}

