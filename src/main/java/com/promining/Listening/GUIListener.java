package com.promining.Listening;

import com.promining.Data;
import com.promining.GUI.GUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static com.promining.Data.playerOpenListGUIData;
import static com.promining.Data.playerOpenPage;
import static com.promining.GUI.GUIManager.openGUI;

public class GUIListener implements Listener {
    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!Data.playerOpenGUI.containsKey(player)) return;
        var data = Data.playerOpenGUI.get(player);
        ItemStack clicked = event.getCurrentItem();
        int slot = event.getSlot();
        switch(data) {
            case LIST -> {
                event.setCancelled(true);
                var listData = playerOpenListGUIData.get(player);
                if(slot == 5*9 && listData.getBackCallBack() != null) {
                    listData.getBackCallBack().run();
                }
                if(slot == 5*9+4 && listData.getCenterCallBack() != null && listData.getCenterInteractButton() != null) {
                    listData.getCenterCallBack().run();
                }
                if(slot < 5*9) {
                    if(listData.getContentCallBack() != null) {
                        listData.getContentCallBack().run(event);
                    }
                }

                var page = playerOpenPage.get(player);
                int maxPage = listData.getItemList().size() / 46;
                if(slot == 5*9+7 && page != 0) {
                    playerOpenPage.replace(player, page-1);
                    openGUI(player, GUIManager.GUI.LIST);
                }
                if(slot == 5*9+8 && page < maxPage) {
                    playerOpenPage.replace(player, page+1);
                    openGUI(player, GUIManager.GUI.LIST);
                }
            }
        }
    }
}
