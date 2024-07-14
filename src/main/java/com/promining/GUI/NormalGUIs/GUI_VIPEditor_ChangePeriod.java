package com.promining.GUI.NormalGUIs;

import com.promining.GUI.GUIAbstract;
import com.promining.GUI.GUIManager;
import com.promining.Useful;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.promining.Data.Data.playerOpenVipData;
import static com.promining.GUI.GUIManager.GUI.EDITOR_VIP_CHANGE_PERIOD;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.Useful.*;

public class GUI_VIPEditor_ChangePeriod extends GUIAbstract {
    @Override
    public GUIManager.GUI getType() {
        return EDITOR_VIP_CHANGE_PERIOD;
    }

    @Override
    public @NotNull Inventory getInventory(Player player) {
        var inv = getInitInventory(9*3, "&c&lVIP期間を変更する");

        var data = playerOpenVipData.get(player);
        var time = getTidyTime(Useful.getHourFromMinute(data.getPeriodPerMinute()) );

        var current = getItem(Material.COMPASS, "&c現在の設定↓");
        setLore(current, List.of(
            "&a"+time
        ));
        inv.setItem(9+4, current);

        var perHour = getItem(Material.STONE_BUTTON, "&c時間単位で設定");
        var perMinute = getItem(Material.OAK_BUTTON, "&c分単位で設定");
        setLore(perHour, List.of(
            "&a現在: " + time,
            "&6左クリックで一時間ずつ増やす",
            "&c右クリックで一時間ずつ減らす"
        ));
        setLore(perMinute, List.of(
            "&a現在: " + time,
            "&6左クリックで一分ずつ増やす",
            "&c右クリックで一分ずつ減らす"
        ));
        inv.setItem(9+3, perHour);
        inv.setItem(9+5, perMinute);
        inv.setItem(0, getItem(Material.FEATHER, "&c戻る"));

        return inv;
    }

    @Override
    public void clickAction(InventoryClickEvent event) {
        var slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        var data = playerOpenVipData.get(player);

        if(slot == 0) {
            openGUI(player, GUIManager.GUI.EDITOR_VIP);
        }
        if(slot == 9+3) {
            if(event.isLeftClick()) {
                data.setPeriodPerMinute( data.getPeriodPerMinute()+60 );
                openGUI(player, getType());
            } else if(event.isRightClick()) {
                if(data.getPeriodPerMinute() <= 0) return;
                data.setPeriodPerMinute( data.getPeriodPerMinute()-60 );
                openGUI(player, getType());
            }
        } else if(slot==9+5) {
            if(event.isLeftClick()) {
                data.setPeriodPerMinute( data.getPeriodPerMinute()+1 );
                openGUI(player, getType());
            } else if(event.isRightClick()) {
                if(data.getPeriodPerMinute() <= 0) return;
                data.setPeriodPerMinute( data.getPeriodPerMinute()-1 );
                openGUI(player, getType());
            }
        }
    }
}
