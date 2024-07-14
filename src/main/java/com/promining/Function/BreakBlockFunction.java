package com.promining.Function;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static com.promining.Data.Data.breakCounterPlayer;
import static com.promining.ProMining.Save;
import static com.promining.Useful.toColor;

public class BreakBlockFunction {
    public static void CountBlock(Player player, String suffix) {
        if(!breakCounterPlayer.containsKey(player.getUniqueId())) {
            breakCounterPlayer.put(player.getUniqueId(), 0L);
        }
        var breakData = breakCounterPlayer.get(player.getUniqueId());
        breakCounterPlayer.replace(player.getUniqueId(), breakData+1);

        if(breakCounterPlayer.get(player.getUniqueId()) % 10000 == 0) {
            Bukkit.broadcastMessage(toColor( "&6&l" + player.getName() + "さんが&c&l破壊数" + breakCounterPlayer.get(player.getUniqueId()) + "回に達成しました！"));
        }

        String actionText = toColor(breakCounterPlayer.get(player.getUniqueId()) + "個" + suffix);
        player.sendTitle("", actionText, 0,30,20);
        Save();
    }
    public static void giveBlock(Player player, BlockBreakEvent event) {
        var inv = player.getInventory();
        String barMessage = "";
        if (inv.firstEmpty() == -1){
            for (ItemStack item : player.getInventory().getContents()) {
                if(item == null) continue;
                if (item.getType() == event.getBlock().getType() && item.getAmount() != 64) {
                    if(item.getAmount() >= 64) {
                    } else {
                        player.getInventory().addItem(new ItemStack( event.getBlock().getType() ));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            barMessage += toColor("&cインベントリがいっぱいなためドロップしました。");
            player.getWorld().dropItem(player.getLocation(), new ItemStack(event.getBlock().getType()));
        } else {
            player.getInventory().addItem( new ItemStack(event.getBlock().getType()));
        }
        player.sendActionBar(toColor(barMessage));
        event.setCancelled(true);
    }
}
