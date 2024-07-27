package com.promining.VillagerScript;

import com.promining.Data.Data;
import com.promining.Data.VillagerData;
import com.promining.GUI.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.UUID;

import static com.promining.Data.Data.playerOpenVillagerEditor;
import static com.promining.GUI.GUIManager.GUI.EDITOR_VILLAGER;
import static com.promining.GUI.GUIManager.GUI.VIP_JOIN_NOT_LIST;
import static com.promining.GUI.GUIManager.ListGUI.VIP_JOIN;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.GUI.GUIManager.openListGUI;

public class VillagerSellerEvents implements Listener {
    @EventHandler
    public void GetDamage(EntityDamageEvent event) {
        //村人のダメージを無効化する
        Entity target = event.getEntity();
        if(Data.VillagerData == null) return;
        if(isTargetVillager(target.getUniqueId()) != null) {
            event.setCancelled(true);
        }
    }
    @EventHandler //ゾンビなどが村人を襲うのを禁止する
    public void ChangeTargetEntity(EntityTargetEvent e) {
        if(e == null) return;
        if(e.getTarget() == null) return;
        if (e.getTarget().getType().equals(EntityType.VILLAGER)) {
            if(isTargetVillager(e.getTarget().getUniqueId()) != null) {
                e.setCancelled(true);
                return;
            }
        }
        e.setCancelled(false);
    }

    @EventHandler
    public void PlayerClickVillager(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        VillagerData villagerData = isTargetVillager(entity.getUniqueId());
        if(villagerData != null) {
            var player = event.getPlayer();

            if(player.isSneaking() && player.hasPermission("promining.editVillager")) {
                playerOpenVillagerEditor.put(player, villagerData);
                Data.playerOpenVipData.put(player, villagerData.getVip());
                openGUI(player, EDITOR_VILLAGER);
                return;
            }
            if(villagerData.getVip() != null) {
                Data.playerOpenVipData.put(player, villagerData.getVip());
                openGUI(player, VIP_JOIN_NOT_LIST);
            } else {
                openListGUI(player, VIP_JOIN);
            }
        }
    }


    public VillagerData isTargetVillager(UUID entityUUID) {
        for(var villager : Data.VillagerData) {
            if(villager.getVillagerUUID().equals(entityUUID)) {
                return villager;
            }
        }
        return null;
    }
}
