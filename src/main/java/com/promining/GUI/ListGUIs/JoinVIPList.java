package com.promining.GUI.ListGUIs;

import com.promining.Data.VIPData;
import com.promining.Function.VIPFunction;
import com.promining.GUI.GUIManager;
import com.promining.GUI.ListGUIAbstract;
import com.promining.System.RunnableSystem;
import com.promining.Useful;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.promining.Data.Data.*;
import static com.promining.Function.NormalBlockFunction.RemoveBlock;
import static com.promining.Function.VIPFunction.JoinVIP;
import static com.promining.GUI.GUIManager.openListGUI;
import static com.promining.GUI.GUIManager.updateListGUI;
import static com.promining.ProMining.Save;
import static com.promining.Useful.*;

public class JoinVIPList extends ListGUIAbstract {
    @Override
    protected GUIManager.ListGUI guiType() {
        return GUIManager.ListGUI.VIP_JOIN;
    }

    @Override
    protected @NotNull String title() {
        return "&c&lVIPに加入する";
    }

    @Override
    public List<ItemStack> itemList() {
        var itemList = new ArrayList<ItemStack>();
        for(var vip : vipData) {
            itemList.add(VIPFunction.getVIPInfoItemStack(player, vip));
        }
        return itemList;
    }

    @Override
    protected ItemStack centerInteractButton() {
        return null;
    }

    @Override
    protected RunnableSystem.Runnable contentCallBack() {
        return (Object e) -> {
            if(e instanceof InventoryClickEvent event) {
                Player player = (Player) event.getWhoClicked();
                var index = getIndexWithSlot(player, event.getSlot());
                playerOpenVipData.put(player, vipData.get(index));
                openListGUI(player, GUIManager.ListGUI.VIP_BLOCK_LIST_FOR_CUSTOMERS);
            }
        };
    }

    @Override
    protected Runnable centerCallBack() {
        return null;
    }

    @Override
    protected Runnable backCallBack() {
        return null;
    }
}
