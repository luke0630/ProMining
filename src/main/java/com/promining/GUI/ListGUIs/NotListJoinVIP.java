package com.promining.GUI.ListGUIs;

import com.promining.Data.Data;
import com.promining.Function.VIPFunction;
import com.promining.GUI.GUIAbstract;
import com.promining.GUI.GUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.GUI.GUIManager.openListGUI;
import static com.promining.Useful.getInitInventory;
import static com.promining.Useful.getItem;

public class NotListJoinVIP extends GUIAbstract {
    @Override
    public GUIManager.GUI getType() {
        return GUIManager.GUI.VIP_JOIN_NOT_LIST;
    }

    @Override
    public @NotNull Inventory getInventory(Player player) {
        var vip = Data.playerOpenVipData.get(player);
        var inv = getInitInventory(9*3, "&c&l" + vip.getVipName());
        inv.setItem(9+4, VIPFunction.getVIPInfoItemStack(player, vip));
        return inv;
    }

    @Override
    public void clickAction(InventoryClickEvent event) {
        var slot = event.getSlot();
        var player = (Player) event.getWhoClicked();
        if(slot == 9+4) {
            openListGUI(player, GUIManager.ListGUI.VIP_BLOCK_LIST_FOR_CUSTOMERS);
        }
    }
}
