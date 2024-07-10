package com.promining.GUI.NormalGUIs;

import com.promining.GUI.GUIAbstract;
import com.promining.GUI.GUIManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.promining.Data.Data.playerOpenVipData;
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
        var titleItem = getItem(Material.GOLD_BLOCK, vipData.getVipName());
        var changePeriod = getItem(Material.COMPASS, "&cVIP適用期間を変更する");

        var period = getHourFromMinute(vipData.getPeriodPerMinute());
        setLore(titleItem, List.of(
                "&aブロック数: " + vipData.getBlockList().size() + "個",
                "&6必要なお金: " + vipData.getNeedYen() + "円",
                "&fVIP適用期間: " + getTidyTime(period)
        ));

        setLore(miningBlockList, List.of(
                "&cクリックしてマイニングブロックリストへ移動"
        ));

        setLore(changePeriod, List.of(
                "&cクリックして期間変更画面に移動"
        ));

        inv.setItem(9+2, miningBlockList);
        inv.setItem(9+6, changePeriod);
        inv.setItem(9+4, titleItem);
        return inv;
    }

    @Override
    public void clickAction(InventoryClickEvent event) {
        var slot = event.getSlot();
        if(slot == 9+2) {

        } else if (slot == 9+4) {

        } else if(slot == 9+6) {

        }
    }
}
