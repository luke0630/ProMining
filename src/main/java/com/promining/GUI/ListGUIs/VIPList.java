package com.promining.GUI.ListGUIs;

import com.promining.Data.Data;
import com.promining.Function.VIPFunction;
import com.promining.GUI.GUIManager;
import com.promining.GUI.ListGUIAbstract;
import com.promining.System.RunnableSystem;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.promining.Data.Data.playerOpenVipData;
import static com.promining.Function.VIPFunction.AddVIP;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.GUI.GUIManager.updateListGUI;
import static com.promining.Useful.*;

public class VIPList extends ListGUIAbstract {
    @Override
    protected @NotNull GUIManager.ListGUI guiType() {
        return GUIManager.ListGUI.VIP_LIST;
    }

    @Override
    protected @NotNull String title() {
        return "&8&lVIPリスト";
    }

    @Override
    public List<ItemStack> itemList() {
        List<ItemStack> itemList = new ArrayList<>();
        for(var vipData : Data.vipData) {
            var item = getItem(vipData.getVipIcon(), vipData.getVipName());
            int blockCount = vipData.getBlockList().size();
            setLore(item, List.of(
                    "&aブロック数: " + blockCount + "個",
                    "&6左クリックで設定画面を開く",
                    "&c右クリックで削除する"
            ));
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    protected ItemStack centerInteractButton() {
        return getItem(Material.REDSTONE_BLOCK, "&6&lVIPを追加する");
    }

    @Override
    protected RunnableSystem.Runnable contentCallBack() {
        return (Object e) -> {
            if(e instanceof InventoryClickEvent event) {
                var vipIndex = getIndexWithSlot(player, event.getSlot());
                var vip = Data.vipData.get(vipIndex);
                if(vip == null) return;

                if(event.isRightClick()) {
                    VIPFunction.RemoveVIP(vip);
                    updateListGUI(player, guiType());
                    return;
                }

                playerOpenVipData.put(player, vip);
                openGUI(player, GUIManager.GUI.EDITOR_VIP);
            }
        };
    }

    @Override
    protected Runnable centerCallBack() {
        return () -> {
            AddVIP();
            updateListGUI(player, guiType());
        };
    }

    @Override
    protected Runnable backCallBack() {
        return () -> {
            openGUI(player, GUIManager.GUI.MAIN_MENU);
        };
    }
}
