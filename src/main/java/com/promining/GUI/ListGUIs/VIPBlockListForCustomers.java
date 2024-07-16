package com.promining.GUI.ListGUIs;

import com.promining.Data.Data;
import com.promining.GUI.GUIManager;
import com.promining.GUI.ListGUIAbstract;
import com.promining.System.RunnableSystem;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.promining.Data.Data.*;
import static com.promining.Function.NormalBlockFunction.RemoveBlock;
import static com.promining.GUI.GUIManager.*;
import static com.promining.ProMining.Save;
import static com.promining.Useful.*;

public class VIPBlockListForCustomers extends ListGUIAbstract {
    @Override
    protected GUIManager.ListGUI guiType() {
        return GUIManager.ListGUI.VIP_BLOCK_LIST_FOR_CUSTOMERS;
    }

    @Override
    protected @NotNull String title() {
        return "&9" + Data.playerOpenVipData.get(player).getVipName() + "で掘られるブロック";
    }

    @Override
    public List<ItemStack> itemList() {
        var itemList = new ArrayList<ItemStack>();
        for(var block : Data.playerOpenVipData.get(player).getBlockList()) {
            var item = new ItemStack(block.getType());
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    protected ItemStack centerInteractButton() {
        var item = getItem(Material.REDSTONE, "&c加入する");
        var vip = playerOpenVipData.get(player);

        String isCanMessage = "";
        Double nokori = isCanPay(player, vip.getNeedYen().doubleValue());
        if(nokori != null) {
            isCanMessage = "&a&l支払い後の所持金: " + nokori + "円";
        } else {
            isCanMessage = "&c&lお金が足りないため加入できません。";
        }
        setLore(item, List.of(
                "&6&l必要なお金: " + vip.getNeedYen() + "円",
                isCanMessage
        ));
        return item;
    }

    @Override
    protected RunnableSystem.Runnable contentCallBack() {
        return null;
    }

    @Override
    protected Runnable centerCallBack() {
        return () -> {
            openGUI(player, GUI.JOIN_VIP_CONFIRM);
        };
    }

    @Override
    protected Runnable backCallBack() {
        return () -> {
            openListGUI(player, GUIManager.ListGUI.VIP_JOIN);
        };
    }
}
