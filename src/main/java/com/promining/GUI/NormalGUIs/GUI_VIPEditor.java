package com.promining.GUI.NormalGUIs;

import com.promining.Data.Data;
import com.promining.GUI.GUIAbstract;
import com.promining.GUI.GUIManager;
import com.promining.ProMining;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.promining.Data.Data.*;
import static com.promining.Function.VIPFunction.ApplySelectorForVIP;
import static com.promining.Function.VIPFunction.SetVIPDescription;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.GUI.GUIManager.openListGUI;
import static com.promining.ProMining.Save;
import static com.promining.Useful.*;

public class GUI_VIPEditor extends GUIAbstract {
    @Override
    public GUIManager.GUI getType() {
        return GUIManager.GUI.EDITOR_VIP;
    }

    @Override
    public @NotNull Inventory getInventory(Player player) {
        var vipData = playerOpenVipData.get(player);
        var inv = getInitInventory(9*3, vipData.getVipName() + " の設定");
        var miningBlockList = getItem(Material.STONE, "&6マイニングブロックリスト");
        var titleItem = getItem(vipData.getVipIcon(), vipData.getVipName());
        var changePeriod = getItem(Material.COMPASS, "&cVIP適用期間を変更する");
        var changeCurrentSelector = getItem(Material.DIAMOND_PICKAXE, "&c現在の選択範囲をこのVIPの範囲に設定する");
        var period = getHourFromMinute(vipData.getPeriodPerMinute());
        var changeCost = getItem(Material.GOLD_INGOT, "&6&l金額を変更");

        setLore(changeCost, List.of(
                "&c現在の金額: " + vipData.getNeedYen() + "円"
        ));

        if(vipData.getSelectorData() != null) {
            setLore(changeCurrentSelector, List.of(
                    "開始地点: " + vipData.getSelectorData().getStart(),
                    "終了地点: " + vipData.getSelectorData().getEnd()
            ));
        } else {
            setLore(changeCurrentSelector, List.of(
                    "選択範囲が指定されていません。"
            ));
        }
        setLore(titleItem, List.of(
                "&aブロック数: " + vipData.getBlockList().size() + "個",
                "&6必要なお金: " + vipData.getNeedYen() + "円",
                "&fVIP適用期間: " + getTidyTime(period),
                "&c説明: " + vipData.getDescription(),
                "&f--------------------------------",
                "&cクリックしてアイコンを変更する",
                "&f--------------------------------"
        ));

        setLore(miningBlockList, List.of(
                "&cクリックしてマイニングブロックリストへ移動"
        ));

        setLore(changePeriod, List.of(
                "&cクリックして期間変更画面に移動"
        ));

        inv.setItem(9*2+4, changeCurrentSelector);
        inv.setItem(9+2, miningBlockList);
        inv.setItem(9+6, changePeriod);
        inv.setItem(9+8, changeCost);
        inv.setItem(9+4, titleItem);
        inv.setItem(0, getItem(Material.FEATHER, "&c戻る"));
        inv.setItem(4, getItem(Material.REDSTONE_BLOCK, "&c説明文を変更する"));
        return inv;
    }

    @Override
    public void clickAction(InventoryClickEvent event) {
        var slot = event.getSlot();
        var player = (Player) event.getWhoClicked();
        if(slot == 0) {
            openListGUI(player, GUIManager.ListGUI.VIP_LIST);
        } else if(slot == 4) {
            SetVIPDescription(player, playerOpenVipData.get(player));
        }
        if(slot == 9+2) {
            openListGUI(player, GUIManager.ListGUI.VIP_BLOCK_LIST);
        } else if(slot == 9+4) {
            openListGUI(player, GUIManager.ListGUI.MATERIAL_LIST);
        }
        else if (slot == 9*2+4) {
            ApplySelectorForVIP(player, playerOpenVipData.get(player));
        } else if(slot == 9+6) {
            openGUI(player, GUIManager.GUI.EDITOR_VIP_CHANGE_PERIOD);
        } else if(slot == 9+8) {
            var vip = playerOpenVipData.get(player);
            getMessageMap.put(player, (Object o) -> {
                if(o instanceof String message) {
                    try {
                        int nextMoney = Integer.parseInt(message);
                        vip.setNeedYen(nextMoney);
                        Bukkit.getScheduler().runTask(ProMining.instance, () -> {
                            openGUI(player, GUIManager.GUI.EDITOR_VIP);
                            Save();
                        });
                    } catch (Exception ignored) {
                        player.sendMessage(toColor("&c&lそれは数字ではありません！"));
                    }
                }
            });
            player.sendMessage(toColor("&c&l金額を入力してください！"));
            player.closeInventory();
        }
    }
}
