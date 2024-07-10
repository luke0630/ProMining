package com.promining.Listening;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static com.promining.Data.Data.*;
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
        } else {
            if(isMarkedBlock(event.getBlock().getLocation())) {
                if(!breakCounterPlayer.containsKey(player.getUniqueId())) {
                    breakCounterPlayer.put(player.getUniqueId(), 0L);
                }
                var breakData = breakCounterPlayer.get(player.getUniqueId());
                breakCounterPlayer.replace(player.getUniqueId(), breakData+1);

                if(breakCounterPlayer.get(player.getUniqueId()) % 10000 == 0) {
                    Bukkit.broadcastMessage(toColor( "&6&l" + player.getName() + "さんが&c&l破壊数" + breakCounterPlayer.get(player.getUniqueId()) + "回に達成しました！"));
                }

                Save();

                String actionText = toColor("&f&l破壊数: " + breakCounterPlayer.get(player.getUniqueId()) + "個");
                player.sendActionBar(actionText);

                var inv = player.getInventory();
                if (inv.firstEmpty() == -1){
                    for (ItemStack item : player.getInventory().getContents()) {
                        if(item == null) continue;
                        if (item.getType() == clickedBlock.getType() && item.getAmount() != 64) {
                            if(item.getAmount() >= 64) {
                            } else {
                                player.getInventory().addItem(new ItemStack( event.getBlock().getType() ));
                                event.setCancelled(true);
                                player.sendActionBar(actionText);
                                return;
                            }
                        }
                    }
                    actionText += toColor(" / &cインベントリがいっぱいなためドロップしました。");
                    player.getWorld().dropItem(player.getLocation(), new ItemStack(event.getBlock().getType()));
                } else {
                    player.getInventory().addItem( new ItemStack(clickedBlock.getType()));
                }
                event.setCancelled(true);
                player.sendActionBar(actionText);
                return;
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if(player.hasPermission("promining.miningotherblock")) return;
        if(event.getClickedBlock() != null) {
            if(!isMarkedBlock(event.getClickedBlock().getLocation())) {
                event.setCancelled(true);
            } else {
                return;
            }
        }
        event.setCancelled(true);
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

}

