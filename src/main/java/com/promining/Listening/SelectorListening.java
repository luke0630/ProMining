package com.promining.Listening;

import com.promining.Data.Data;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.promining.Data.Data.vipData;
import static com.promining.Function.VIPFunction.IsInVIP;
import static com.promining.Function.VIPFunction.StartCount;

public class SelectorListening implements Listener {
    @EventHandler
    public void onPlayerMoving(PlayerMoveEvent event) {
        var player = event.getPlayer();
        for(var vip : Data.vipData) {
            if(IsInVIP(player.getLocation(), vip)) {
                StartCount(player, vip);
            } else {
                var data = vip.getCountData();
                var content = data.get(player.getUniqueId());
                if(content == null) continue;
                if(content.getTask() == null) continue;
                content.getTask().cancel();
                content.setTask(null);
            }
        }
    }
}
