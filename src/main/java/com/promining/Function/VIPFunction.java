package com.promining.Function;

import com.promining.Data.Data;
import com.promining.Data.VIPData;
import com.promining.ProMining;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.promining.Data.Data.actionBarTaskList;
import static com.promining.Data.Data.vipData;
import static com.promining.Useful.*;

public class VIPFunction {
    public static void RemoveVIP(VIPData vipData) {
        Data.vipData.remove(vipData);
    }
    public static VIPData AddVIP() {
        var newData = new VIPData();
        String newName = newData.getVipName();
        int count = 0;
        for(var data : vipData) {
            if(data.getVipName().equalsIgnoreCase(newName)) {
                count++;
                newName = newData.getVipName() + "(" + count + ")";
            }
        }
        newData.setVipName(newName);
        vipData.add(newData);
        return newData;
    }

    public static void StartCount(Player player) {
        for(var vip : vipData) {
            var data = vip.getCountData();
            if(data.containsKey(player.getUniqueId())) {
                if(data.get(player.getUniqueId()).getTask() == null) {
                    Bukkit.getScheduler().runTaskTimerAsynchronously(ProMining.instance, getCounter(player, vip), 0, 20);
                }
            }
        }
    }

    public static void ShowBar(Player player) {
        actionBarTaskList.put(
            player,
            Bukkit.getScheduler().runTaskTimerAsynchronously(ProMining.instance, () -> {
                String barMessage = "";
                for(var vip : vipData) {
                    if(vip.getCountData().containsKey(player.getUniqueId())) {
                        var countData = vip.getCountData().get(player.getUniqueId());
                        String time = getTidyTime( getTheTimeFromSecond(countData.getVipPeriodTime().intValue()) );
                        barMessage += "【" + vip.getVipName() + ": " + time + "】"  ;
                    } else {
                        return;
                    }
                }
                String finalBarMessage = barMessage;
                Bukkit.getScheduler().runTask(ProMining.instance, () -> {
                    player.sendActionBar(finalBarMessage);
                });
            },0 ,20L)
        );
    }

    public static void JoinVIP(VIPData vip, Player player) {
        if(vip.getCountData().containsKey(player.getUniqueId())) {
            player.sendMessage(toColor("&cすでにそのVIPに入っています。"));
            return;
        }
        vip.getCountData().put(player.getUniqueId(), new Data.CountData(
                Bukkit.getScheduler().runTaskTimerAsynchronously(ProMining.instance, getCounter(player, vip), 0, 20),
                vip.getPeriodPerMinute().longValue()*60
        ));

        player.sendMessage("-------------------------------------------");
        player.sendMessage(toColor("&c" + vip.getVipName() + " のVIPになりました！"));
        player.sendMessage(toColor("&a対象のVIPゾーンに入っている間は、使用期限が近づきます。"));
        player.sendMessage(toColor("&c使用期間がすぎると対象のVIPマイニングはできなくなります。"));
        player.sendMessage("-------------------------------------------");
    }

    static Runnable getCounter(Player player, VIPData vip) {
        var data = vip.getCountData();
        return () -> {
            if(data.get(player.getUniqueId()).getVipPeriodTime() == null) return;
            if(data.get(player.getUniqueId()).getVipPeriodTime() <= 0) {
                vip.getCountData().get(player.getUniqueId()).getTask().cancel();
                vip.getCountData().remove(player.getUniqueId());
                player.sendMessage("-------------------------------------------");
                player.sendMessage(toColor("&c" + vip.getVipName() + " の期限が切れました！このVIPのサービスは使用できなくなります。"));
                player.sendMessage(toColor("&aもう一度使用するにはもう一度購入しなおしてください。"));
                player.sendMessage("-------------------------------------------");
                return;
            }
            data.get(player.getUniqueId()).setVipPeriodTime( data.get(player.getUniqueId()).getVipPeriodTime()-1 );
        };
    }
}
