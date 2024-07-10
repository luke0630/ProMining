package com.promining.Listening;

import com.promining.Data.Data;
import com.promining.GUI.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static com.promining.Data.Data.playerOpenListGUIData;
import static com.promining.Data.Data.playerOpenPage;
import static com.promining.GUI.GUIManager.openGUI;

public class GUIListener implements Listener {
    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!Data.playerOpenGUI.containsKey(player)) return;
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getType() == Material.AIR) return;
        var data = Data.playerOpenGUI.get(player);

        for(var gui : GUIManager.GUIs) {
            if(gui.getType() == data) {
                gui.clickAction(event);
            }
        }
    }
}
