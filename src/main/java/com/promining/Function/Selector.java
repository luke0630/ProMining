package com.promining.Function;

import com.promining.Useful;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Selector {
    static final String WAND_LORE = "PROMINING";
    public static void giveWand(Player player) {
        player.getInventory().addItem(getItemStackWand());
    }

    static ItemStack getItemStackWand() {
        var item = Useful.getItem(Material.DIAMOND_PICKAXE, "範囲選択ツール");

        Useful.setLore(item,
            List.of(
                WAND_LORE
            )
        );

        return item;
    }

    public static boolean isWand(ItemStack itemStack) {
        if(itemStack.getType() != Material.DIAMOND_PICKAXE) return false;
        if(itemStack.getLore() == null) return false;
        if(itemStack.getLore().get(0).equalsIgnoreCase(WAND_LORE)) return true;
        return false;
    }
}
