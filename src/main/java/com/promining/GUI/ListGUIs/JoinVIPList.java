package com.promining.GUI.ListGUIs;

import com.promining.Data.VIPData;
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
            var item = getItem(Material.GOLD_BLOCK, "&6" + vip.getVipName());
            setLore(item, List.of(
                    "&f&l-------------------------------------",
                    "&cVIP期限: " + Useful.getTidyTime( Useful.getHourFromMinute(vip.getPeriodPerMinute()) ),
                    "&a加入金: " + vip.getNeedYen() + "円",
                    "&c説明: " + vip.getDescription(),
                    "&f&l-------------------------------------",
                    "&cクリックして加入する",
                    "&f&l-------------------------------------",
                    "&6VIP期限とは: このVIPでいられる期間のことで、",
                    "&6&l期間が過ぎると&c&lVIPのブロックは手に入れられなくなります。",
                    "&6もう一度加入すればまた掘られるようになります。",
                    "&6&l期限は対象のVIPエリアにいる間は期限が迫っていきます。"
            ));
            itemList.add(item);
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
                JoinVIP(vipData.get(index), player);
                player.closeInventory();
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
