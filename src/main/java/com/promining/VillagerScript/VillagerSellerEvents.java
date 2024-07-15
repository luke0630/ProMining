package com.promining.VillagerScript;

import com.promining.Data.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static com.promining.GUI.GUIManager.ListGUI.VIP_JOIN;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.GUI.GUIManager.openListGUI;

public class VillagerSellerEvents implements Listener {
    @EventHandler
    public void GetDamage(EntityDamageEvent event) {
        //村人のダメージを無効化する
        Entity target = event.getEntity();
        if(Data.VillagerData == null) return;
        if(Data.VillagerData.getVillagerUUID().equals(target.getUniqueId())) {
            event.setCancelled(true);
        }
    }
    @EventHandler //ゾンビなどが村人を襲うのを禁止する
    public void ChangeTargetEntity(EntityTargetEvent e) {
        if(Data.VillagerData == null) return;
        if(e == null) return;
        if(e.getTarget() == null) return;
        if (e.getTarget().getType().equals(EntityType.VILLAGER)) {
            if(Data.VillagerData.getVillagerUUID().equals(e.getTarget().getUniqueId())) {
                e.setCancelled(true);
                return;
            }
        }
        e.setCancelled(false);
    }

    @EventHandler
    public void PlayerClickVillager(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if(Data.VillagerData == null) return;
        if(Data.VillagerData.getVillagerUUID().equals(entity.getUniqueId())) {
            var player = event.getPlayer();
            openListGUI(player, VIP_JOIN);
        }
    }
}
