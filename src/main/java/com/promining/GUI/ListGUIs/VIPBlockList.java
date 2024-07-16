package com.promining.GUI.ListGUIs;

import com.promining.Data.Data;
import com.promining.Data.VIPData;
import com.promining.Function.VIPFunction;
import com.promining.GUI.GUIManager;
import com.promining.GUI.ListGUIAbstract;
import com.promining.System.RunnableSystem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.promining.Data.Data.*;
import static com.promining.Function.NormalBlockFunction.RemoveBlock;
import static com.promining.GUI.GUIManager.*;
import static com.promining.Listening.Listener.isVIPMarkedBlock;
import static com.promining.ProMining.Save;
import static com.promining.Useful.*;
import static com.promining.Useful.toColor;

public class VIPBlockList extends ListGUIAbstract {
    VIPData vipData = null;
    @Override
    protected GUIManager.ListGUI guiType() {
        return GUIManager.ListGUI.VIP_BLOCK_LIST;
    }

    @Override
    protected @NotNull String title() {
        return "&8&lブロックリスト";
    }

    @Override
    public List<ItemStack> itemList() {
        var itemList = new ArrayList<ItemStack>();
        vipData = playerOpenVipData.get(player);
        for(var block : vipData.getBlockList()) {
            var item = new ItemStack(block.getType());
            setLore(
                    item, List.of("&f&l"+getTidyLocation(block.getLocation()), "&a左クリックして対象にテレポート", "&c右クリックで対象を削除")
            );
            itemList.add( item );
        }
        return itemList;
    }

    @Override
    protected ItemStack centerInteractButton() {
        return getItem(Material.REDSTONE_BLOCK, "&cブロックを追加する");
    }

    @Override
    protected RunnableSystem.Runnable contentCallBack() {
        return (Object e) -> {
            if(e instanceof InventoryClickEvent event) {
                try {
                    int minIndex = playerOpenPage.get(player) * 45;
                    var data = vipData.getBlockList().get(minIndex + event.getSlot());
                    if(event.isLeftClick()) {
                        player.teleport(data.getLocation());
                        player.sendMessage(data.getType() + "にテレポートしました。");
                        player.closeInventory();
                    } else if(event.isRightClick()) {
                        RemoveBlock(data.getLocation());
                        player.sendMessage(toColor("&c" + data.getType() + "を削除しました。"));
                        updateListGUI(player, guiType());
                        Save();
                    }
                } catch (Exception ignored) {

                }
            }
        };
    }

    @Override
    protected Runnable centerCallBack() {
        return () -> {
            var data = playerOpenVipData.get(player).clone();
            vipMarkingPlayer.remove(player);
            clickedMiningBlock.remove(player);
            getClickedBlock(player, (Object o) -> {
                if(o instanceof Block block) {
                    for(var markedBlock : markedBlockList) {
                        if(markedBlock.getLocation().equals(block.getLocation())) {
                            player.sendMessage(toColor("&cそのブロックはすでに登録済みです。"));
                            return;
                        } else if(markedBlock.getType().equals(block.getType())) {
                            player.sendMessage(toColor("&cそのブロックタイプはすでにノーマルに存在するため追加できませんでした。"));
                            return;
                        }
                    }
                    var vip = isVIPMarkedBlock(block.getLocation());
                    if(vip != null) {
                        player.sendMessage(toColor("&cそのブロックはすでに&6" + vip.getVipName() + "&cのブロックになっています。"));
                        return;
                    }
                    for(var vipData : Data.vipData) {
                        if(data.equals(vipData)) break;
                        for(var blockData : vipData.getBlockList()) {
                            if(block.getType().equals(blockData.getType())) {
                                player.sendMessage(toColor("&cそのブロックタイプはすでに&6" + vipData.getVipName() +"&cに存在するため追加できませんでした。"));
                                return;
                            }
                        }
                    }
                    data.getBlockList().add(block);
                    player.sendMessage("&c&lVIPブロックを追加しました");
                    openListGUI(player, ListGUI.VIP_BLOCK_LIST);
                }
            });
            player.sendMessage(toColor("&cVIPマイニング対象にしたいブロックをクリックしてください。"));
            player.closeInventory();
        };
    }

    @Override
    protected Runnable backCallBack() {
        return () -> {
            GUIManager.openGUI(player, GUIManager.GUI.EDITOR_VIP);
        };
    }
}
