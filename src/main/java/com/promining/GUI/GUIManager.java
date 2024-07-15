package com.promining.GUI;

import com.promining.GUI.ListGUIs.BlockList;
import com.promining.GUI.ListGUIs.JoinVIPList;
import com.promining.GUI.ListGUIs.VIPBlockList;
import com.promining.GUI.ListGUIs.VIPList;
import com.promining.GUI.NormalGUIs.GUI_List;
import com.promining.GUI.NormalGUIs.GUI_VIPEditor;
import com.promining.GUI.NormalGUIs.GUI_VIPEditor_ChangePeriod;
import com.promining.GUI.NormalGUIs.MAIN_MENU;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static com.promining.Function.ListFunction.getMaxPage;
import static com.promining.Useful.*;
import static com.promining.Data.Data.*;

public class GUIManager {
    public enum GUI {
        MAIN_MENU,
        LIST,
        EDITOR_VIP,
        EDITOR_VIP_CHANGE_PERIOD,
    }
    public enum ListGUI {
        BLOCK_LIST,
        VIP_LIST,
        VIP_BLOCK_LIST,
        VIP_JOIN,
    }
    public static final ListGUIAbstract[] ListGUIs = {
            new BlockList(),
            new VIPList(),
            new VIPBlockList(),
            new JoinVIPList(),
    };
    public static final GUIAbstract[] GUIs = {
            new GUI_List(),
            new MAIN_MENU(),
            new GUI_VIPEditor(),
            new GUI_VIPEditor_ChangePeriod(),
    };
    static GUIListData getListGUI(Player player, ListGUI gui) {
        for(var guiData : ListGUIs) {
            if(guiData.guiType() == gui) {
                return guiData.getGUIListData(player);
            }
        }
        return null;
    }
    public static void openListGUI(Player player, ListGUI gui) {
        playerOpenPage.put(player, 0);
        playerOpenListGUIData.put(player,  getListGUI(player,gui));
        openGUI(player, GUI.LIST);
    }

    private static Inventory getGUI(Player player, GUI gui) {
        for(var guiData : GUIs) {
            if(guiData.getType() == gui) {
                return guiData.getInventory(player);
            }
        }
        player.sendMessage("getGUIでNULLが発生しています。GUIをfinalの変数に代入してください。");
        return null;
    }
    public static void openGUI(Player player, GUI gui) {
        playerOpenGUI.put(player, gui);
        player.openInventory(getGUI(player, gui));
        playerOpenGUI.put(player, gui);
    }

    public static void updateListGUI(Player player,ListGUI gui) {
        if(!playerOpenListGUIData.containsKey(player)) return;
        var listData = getListGUI(player, gui);
        if(listData != null) {
            var newItemList = listData.getItemList();
            playerOpenListGUIData.get(player).setItemList(newItemList);

            var maxPage = getMaxPage(player);
            if(maxPage != null && maxPage < playerOpenPage.get(player)) {
                playerOpenPage.replace(player, maxPage);
            }

            openGUI(player, GUI.LIST);
        }

    }
}

