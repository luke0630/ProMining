package com.promining.Listening;

import com.promining.Data.Data;
import com.promining.Data.VIPData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.promining.Data.Data.vipData;
import static com.promining.Function.VIPFunction.IsInVIP;
import static com.promining.Function.VIPFunction.StartCount;
import static com.promining.Useful.toColor;

public class SelectorListening implements Listener {
    Map<Player, VIPData> inVIPList = new HashMap<>();
    @EventHandler
    public void onPlayerMoving(PlayerMoveEvent event) {
        var player = event.getPlayer();
        for(var vip : Data.vipData) {
            if(IsInVIP(player.getLocation(), vip)) {
                if(!inVIPList.containsKey(player) || inVIPList.get(player) != vip) {
                    inVIPList.put(player, vip);
                    player.sendMessage(toColor("&a&l" + vip.getVipName() + "のエリアに入りました。"));
                }
                StartCount(player, vip);
            } else {
                var data = vip.getCountData();
                var content = data.get(player.getUniqueId());
                if(content == null) continue;
                if(content.getTask() == null) continue;
                content.getTask().cancel();
                content.setTask(null);
                if(inVIPList.containsKey(player)) {
                    inVIPList.remove(player);
                    player.sendMessage(toColor("&c&l" + vip.getVipName() + "のエリアから出ました。"));
                }
            }
        }
    }
}
