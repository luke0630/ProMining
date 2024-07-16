package com.promining.GUI.NormalGUIs;

import com.promining.Data.Data;
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
import static com.promining.Data.Data.vipData;
import static com.promining.Function.VIPFunction.JoinVIP;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.GUI.GUIManager.openListGUI;
import static com.promining.ProMining.getEcon;
import static com.promining.Useful.*;

public class GUI_VipConfirm extends GUIAbstract {
    @Override
    public GUIManager.GUI getType() {
        return GUIManager.GUI.JOIN_VIP_CONFIRM;
    }

    @Override
    public @NotNull Inventory getInventory(Player player) {
        var vipData = Data.playerOpenVipData.get(player);
        var inv = getInitInventory(9*3, "&c&lVIP加入確認画面 - " + vipData.getVipName());

        var join = getItem(Material.GOLD_BLOCK, "&c加入する");
        Double joinMoney = Useful.isCanPay(player, vipData.getNeedYen().doubleValue());
        String joinString = "";
        if(joinMoney != null) {
            joinString = "&6&l加入後のお金: " + joinMoney + "円";
        } else {
            joinString = "&c&l加入できるお金がありません。";
        }
        setLore(join, List.of(
                "&6&l加入金: " + vipData.getNeedYen() + "円",
                "&c&l所持金: " + getEcon().getBalance(player) + "円",
                joinString
        ));
        inv.setItem(9+3, join);
        inv.setItem(9+5, getItem(Material.FEATHER, "&9やめる"));
        return inv;
    }

    @Override
    public void clickAction(InventoryClickEvent event) {
        var slot = event.getSlot();
        var player = (Player) event.getWhoClicked();
        if(slot == 9+3) {
            JoinVIP(playerOpenVipData.get(player), player);
            player.closeInventory();
        } else if(slot == 9+5) {
            openListGUI(player, GUIManager.ListGUI.VIP_BLOCK_LIST_FOR_CUSTOMERS);
        }
    }
}
