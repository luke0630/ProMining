package com.promining.Listening;
import com.promining.Data.Data;
import com.promining.Data.VIPData;
import com.promining.Function.BreakBlockFunction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static com.promining.Data.Data.*;
import static com.promining.Data.Data.vipData;
import static com.promining.Function.VIPFunction.ShowBar;
import static com.promining.ProMining.Save;
import static com.promining.Useful.*;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        playerOpenGUI.remove( (Player) event.getPlayer());
    }
    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        var player = event.getPlayer();
        var clickedBlock = event.getBlock();

        if(clickedMiningBlock.containsKey(player)) {
            if (clickedBlock.isEmpty()) return;
            clickedMiningBlock.get(player).run(clickedBlock);
            clickedMiningBlock.remove(player);
            event.setCancelled(true);
            return;
        }
        var clickedVIPData = isVIPMarkedBlock(event.getBlock().getLocation());
        if(clickedVIPData != null) {
            for(var vip : vipData) {
                if(!clickedVIPData.equals(vip)) break;
                if(vip.getCountData().containsKey(player.getUniqueId())) {
                    BreakBlockFunction.CountBlock(player," &6" + clickedVIPData.getVipName());
                    BreakBlockFunction.giveBlock(player, event);
                    event.setCancelled(true);
                    return;
                }
            }
            event.setCancelled(true);
            player.sendMessage(toColor("&6" + clickedVIPData.getVipName() + " &cのVIPに加入していないためそれは無効です！"));
            return;
        }

        if(markingPlayer.containsKey(player)) {
            if(clickedBlock.isEmpty()) return;

            for(var data : markedBlockList) {
                if(data.getLocation().equals(event.getBlock().getLocation())) {
                    player.sendMessage(toColor("&cその座標のブロックはすでにマーク済みですが、ブロックを置き換えました。"));
                    markedBlockList.remove(data);
                    break;
                }
            }

            markedBlockList.add(clickedBlock);

            if(markingPlayer.get(player)) {
                player.sendMessage("追加しました。続けてクリックするか、/mining markmodeで抜け出してください。");
            } else {
                markingPlayer.remove(player);
                player.sendMessage("追加しました");
            }
            event.setCancelled(true);
            Save();
            return;
        } else {
            if(isMarkedBlock(event.getBlock().getLocation())) {
                BreakBlockFunction.CountBlock(player, "");
                BreakBlockFunction.giveBlock(player, event);
                event.setCancelled(true);
                return;
            }
        }

        if(!player.hasPermission("promining.miningotherblock")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event ) {
        var player = event.getPlayer();
        if(clickedMiningBlock.containsKey(player)) return;
        if(player.hasPermission("promining.miningotherblock")) return;
        if(isMarkedBlock(event.getBlock().getLocation()) || isVIPMarkedBlock(event.getBlock().getLocation()) != null) {
            return;
        }
        event.setCancelled(true);
    }

    VIPData isVIPMarkedBlock(Location location) {
        for(var data : vipData) {
            for(var locData : data.getBlockList()) {
                if(locData.getLocation().equals(location)) return data;
            }
        }
        return null;
    }

    VIPData getVIPFromBlock(Location location) {
        for(var data : vipData) {
            for(var block : data.getBlockList()) {
                if(block.getLocation().equals(location)) return data;
            }
        }
        return null;
    }

    boolean isMarkedBlock(Location location) {
        for(var data : markedBlockList) {
            if(data.getLocation().equals(location)) {
                return true;
            }
        }
        return false;
    }
    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        for(var data : event.blockList()) {
            if(isMarkedBlock(data.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPistonEvent(BlockPistonExtendEvent event) {
        for(var data : event.getBlocks()) {
            if(isMarkedBlock(data.getLocation())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPistonRetractEvent(BlockPistonRetractEvent event) {
        for(var data : event.getBlocks()) {
            if(isMarkedBlock(data.getLocation())) {
                event.setCancelled(true);
            }
        }
    }



    @EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent event) {
        if(event.getIgnitingBlock() == null) return;
        if(isMarkedBlock(Objects.requireNonNull(event.getIgnitingBlock()).getLocation())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        markingPlayer.remove(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        ShowBar(player);
    }

}

