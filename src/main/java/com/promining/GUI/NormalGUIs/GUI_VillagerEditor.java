package com.promining.GUI.NormalGUIs;

import com.promining.Data.Data;
import com.promining.Function.VillagerFunction;
import com.promining.GUI.GUIAbstract;
import com.promining.GUI.GUIManager;
import com.promining.ProMining;
import com.promining.Useful;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.promining.Data.Data.getMessageMap;
import static com.promining.Data.Data.playerOpenVillagerEditor;
import static com.promining.GUI.GUIManager.GUI.EDITOR_VILLAGER;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.ProMining.Save;
import static com.promining.Useful.*;

public class GUI_VillagerEditor extends GUIAbstract {
    @Override
    public GUIManager.GUI getType() {
        return EDITOR_VILLAGER;
    }

    @Override
    public @NotNull Inventory getInventory(Player player) {
        var inv = getInitInventory(9*3, "&c&l村人の編集");
        var villagerData = playerOpenVillagerEditor.get(player);

        var changeName = getItem(Material.NAME_TAG, "&c&l名前を変更する");
        setLore(changeName, List.of(
                "&6現在の名前: " + villagerData.getName(),
                "&c&lクリックして変更する"
        ));

        var changePos = getItem(Material.COMPASS, "&a&l場所を変更する");
        setLore(changePos, List.of(
                "&6現在の場所" + Useful.getTidyLocation( villagerData.getLocation() ),
                "&c&lクリックして変更する"
        ));

        var removeVillager = getItem(Material.BARRIER, "&c&lこのショップを削除する");
        setLore(removeVillager, List.of(
                "&c&lクリックして削除する"
        ));


        inv.setItem(9+2, changePos );
        inv.setItem(9+4, changeName );
        inv.setItem(9+6, removeVillager );
        return inv;
    }

    @Override
    public void clickAction(InventoryClickEvent event) {
        var slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();

        player.closeInventory();
        var data = playerOpenVillagerEditor.get(player);
        if(slot == 9+2) {
            player.sendMessage(toColor("&c村人の場所を変更します。変更場所についたら何かしらメッセージを送信してください。"));
            getMessageMap.put(player, (Object o) -> {
                Bukkit.getScheduler().runTask(ProMining.instance, () -> {
                    data.getEntityData().teleport(player.getLocation());
                    player.sendMessage(toColor("&a&l変更しました。"));
                    Save();
                });
            });
        } else if(slot == 9+4) {
            player.sendMessage(toColor("&c村人の名前を変更します。チャットに打ち込んで送信してください。"));
            getMessageMap.put(player, (Object o) -> {
                if(o instanceof String message) {
                    data.setName(toColor(message));
                    data.getEntityData().setCustomName(toColor(message));
                    Bukkit.getScheduler().runTask(ProMining.instance, () -> {
                        player.sendMessage("&c&l名前を変更しました。");
                        Save();
                    });
                }
            });
        } else if(slot == 9+6) {
            data.getEntityData().remove();
            Data.VillagerData.remove(data);
            Save();
            player.sendMessage(toColor("&c&lショップを削除しました！"));
        }
    }
}
