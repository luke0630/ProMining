package com.promining.GUI.NormalGUIs;

import com.promining.GUI.GUIAbstract;
import com.promining.GUI.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static com.promining.Data.Data.playerOpenListGUIData;
import static com.promining.Data.Data.playerOpenPage;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.Useful.*;

public class GUI_List extends GUIAbstract {
    @Override
    public GUIManager.@NotNull GUI getType() {
        return GUIManager.GUI.LIST;
    }

    @Override
    public void clickAction(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
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

    @Override
    public @NotNull Inventory getInventory(Player player) {
        var listData = playerOpenListGUIData.get(player);
        int maxPageData = listData.getItemList().size() / 46;
        var inv = getInitInventory(9*6, listData.getTitle() + playerOpenPage.get(player) + " / " + maxPageData);
        setBottomControlOnGUI(inv);

        if(listData.getBackCallBack() != null) {
            inv.setItem(9*5, getItem(Material.FEATHER, "戻る"));
        }
        if(listData.getCenterCallBack() != null && listData.getCenterInteractButton() != null) {
            inv.setItem(9*5+4, listData.getCenterInteractButton());
        }

        if(maxPageData < 1) {
            //ページなし
            for(int i=0;i < listData.getItemList().size();i++) {
                inv.setItem(i, listData.getItemList().get(i));
            }
        } else {
            if(playerOpenPage.get(player) != 0) {
                inv.setItem(9*5+7, getItem(Material.FEATHER, "&a&l前のページ"));
            }
            if(maxPageData != playerOpenPage.get(player)) {
                inv.setItem(9*5+8, getItem(Material.ARROW, "&c&l次のページ"));
            }
            int minIndex = playerOpenPage.get(player)*45;
            int test=0;
            for(int i=minIndex;i < minIndex+45;i++) {
                try {
                    inv.setItem(test, listData.getItemList().get(i));
                    test++;
                } catch (Exception ignored) {
                    break;
                }
            }
        }
        return inv;
    }
}
