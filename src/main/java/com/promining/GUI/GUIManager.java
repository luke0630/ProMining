package com.promining.GUI;

import com.promining.Data;
import com.promining.System.RunnableSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.promining.ProMining.Save;
import static com.promining.Useful.*;
import static com.promining.Data.*;

public class GUIManager {
    public enum GUI {
        LIST,
    }
    public enum ListGUI {
        BLOCK_LIST
    }

    public static void openListGUI(Player player, ListGUI gui) {
        GUIListData listData;
        String title = "";
        var itemList = new ArrayList<ItemStack>();
        ItemStack centerItem = null;
        Runnable centerCallBack = null;
        Runnable backCallBack = null;
        RunnableSystem.Runnable contentCallBack = null;

        playerOpenPage.put(player, 0);

        switch(gui) {
            case BLOCK_LIST -> {
                title = "&8&lブロックリスト";
                for(var block : markedBlockList) {
                    var item = new ItemStack(block.getType());
                    setLore(
                        item, List.of("&f&l"+getTidyLocation(block.getLocation()), "&a左クリックして対象にテレポート", "&c右クリックで対象を削除")
                    );
                    itemList.add( item );
                }
                centerItem = getItem(Material.REDSTONE, "&cブロックを追加する");
                centerCallBack = () -> {
                    markingPlayer.put(player, false);
                    player.sendMessage(toColor("&cマイニング対象にしたいブロックをクリックしてください。"));
                    player.closeInventory();
                };
                contentCallBack = (Object e) -> {
                    if(e instanceof InventoryClickEvent event) {
                        try {
                            int minIndex = playerOpenPage.get(player) * 45;
                            var data = markedBlockList.get(minIndex + event.getSlot());
                            if(event.isLeftClick()) {
                                player.teleport(data.getLocation());
                                player.sendMessage(data.getType() + "にテレポートしました。");
                                player.closeInventory();
                            } else if(event.isRightClick()) {
                                markedBlockList.remove(data);
                                player.sendMessage(toColor("&c" + data.getType() + "を削除しました。"));
                                playerOpenListGUIData.get(player).getItemList().remove(minIndex + event.getSlot());
                                openGUI(player, GUI.LIST);
                                Save();
                            }
                        } catch (Exception ignored) {

                        }
                    }
                };
            }
        }

        listData = new GUIListData(
                title,
                itemList,
                centerItem,
                contentCallBack,
                centerCallBack,
                backCallBack
        );
        playerOpenListGUIData.put(player, listData);
        openGUI(player, GUI.LIST);
    }

    private static Inventory getGUI(Player player, GUI gui) {
        Inventory inv = Bukkit.createInventory(null, 9*6, "");
        switch(gui) {
            case LIST -> {
                var listData = playerOpenListGUIData.get(player);
                int maxPageData = listData.getItemList().size() / 46;
                inv = Bukkit.createInventory(null, 9*6, listData.getTitle() + playerOpenPage.get(player) + " / " + maxPageData);
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
            }
        }
        return inv;
    }
    public static void openGUI(Player player, GUI gui) {
        playerOpenGUI.put(player, gui);
        player.openInventory(getGUI(player, gui));
        playerOpenGUI.put(player, gui);
    }
}
