package com.promining.GUI.NormalGUIs;

import com.promining.GUI.GUIAbstract;
import com.promining.GUI.GUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static com.promining.Data.Data.playerOpenVipData;
import static com.promining.Useful.getInitInventory;

public class GUI_VIPEditor extends GUIAbstract {
    @Override
    public GUIManager.GUI getType() {
        return GUIManager.GUI.EDITOR_VIP;
    }

    @Override
    public @NotNull Inventory getInventory(Player player) {
        var inv = getInitInventory(9*3, playerOpenVipData.get(player).getVipName() + "設定");
        return inv;
    }

    @Override
    public void clickAction(InventoryClickEvent event) {

    }
}
