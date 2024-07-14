package com.promining.GUI.NormalGUIs;

import com.promining.GUI.GUIAbstract;
import com.promining.GUI.GUIManager;
import com.promining.Useful;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static com.promining.GUI.GUIManager.openListGUI;
import static com.promining.Useful.*;

public class MAIN_MENU extends GUIAbstract {
    @Override
    public @NotNull GUIManager.GUI getType() {
        return GUIManager.GUI.MAIN_MENU;
    }

    @Override
    public @NotNull Inventory getInventory(Player player) {
        var inv = getInitInventory(9*3, "&8&lメインメニュー");
        inv.setItem(9+3, getItem(Material.GRASS_BLOCK, "&aノーマルマイニングブロック一覧"));
        inv.setItem(9+5, getItem(Material.GOLD_BLOCK, "&6VIP一覧"));
        return inv;
    }

    @Override
    public void clickAction(InventoryClickEvent event) {
        var slot = event.getSlot();
        var player = (Player) event.getWhoClicked();

        if(slot == 9+3) {
            openListGUI(player, GUIManager.ListGUI.BLOCK_LIST);
        } else if(slot == 9+5) {
            openListGUI(player, GUIManager.ListGUI.VIP_LIST);
        }
    }
}
