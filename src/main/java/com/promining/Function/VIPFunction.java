package com.promining.Function;

import com.promining.Data.Data;
import com.promining.Data.VIPData;
import com.promining.GUI.GUIManager;
import com.promining.ProMining;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.promining.Data.Data.*;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.ProMining.getEcon;
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
        newData.setDescription("説明なし");
        vipData.add(newData);
        return newData;
    }
    public static void SetVIPDescription(Player player, VIPData data) {
        player.sendMessage(toColor("&cチャットに説明文を入力し送信してください。"));
        player.closeInventory();
        getMessageMap.put(player, (Object o) -> {
            if(o instanceof String message) {
                data.setDescription(message);
                Bukkit.getScheduler().runTask(ProMining.instance, () -> {
                    openGUI(player, GUIManager.GUI.EDITOR_VIP);
                });
            }
        });
    }

    public static void StartCount(Player player, VIPData vip) {
        var data = vip.getCountData();
        var content = data.get(player.getUniqueId());
        if(data.containsKey(player.getUniqueId())) {
            if(content.getTask() == null || content.getTask().isCancelled()) {
                if(content.getTask() != null) {
                    content.getTask().cancel();
                }
                content.setTask(null);
                content.setTask(
                    Bukkit.getScheduler().runTaskTimerAsynchronously(ProMining.instance, getCounter(player, vip), 0, 20)
                );
            }
        }
    }


    public static void UpdateShowBar(Player player, VIPData vip) {
        if(!vip.getCountData().containsKey(player.getUniqueId())) return;
        var countData = vip.getCountData().get(player.getUniqueId());
        String time = getTidyTime( getTheTimeFromSecond(countData.getVipPeriodTime().intValue()) );
        player.sendActionBar("【" + vip.getVipName() + ": " + time + "】");
    }

    public static void JoinVIP(VIPData vip, Player player) {
        if(vip.getCountData().containsKey(player.getUniqueId())) {
            player.sendMessage(toColor("&cすでにそのVIPに入っています。"));
            return;
        }
        if(getEcon().getBalance(player)-vip.getNeedYen() < 0) {
            player.sendMessage(toColor("&c&lお金が足りません！"));
            return;
        }
        getEcon().withdrawPlayer( player, vip.getNeedYen() );
        vip.getCountData().put(player.getUniqueId(), new Data.CountData(
                Bukkit.getScheduler().runTaskTimerAsynchronously(ProMining.instance, getCounter(player, vip), 0, 20),
                vip.getPeriodPerMinute().longValue()*60
        ));

        player.sendMessage("-------------------------------------------");
        player.sendMessage(toColor("&a" + vip.getNeedYen() + "円を支払い、"));
        player.sendMessage(toColor("&c" + vip.getVipName() + " のVIPになりました！"));
        player.sendMessage(toColor("&a対象のVIPゾーンに入っている間は、使用期限が近づきます。"));
        player.sendMessage(toColor("&c使用期間がすぎると対象のVIPマイニングはできなくなります。"));
        player.sendMessage("-------------------------------------------");
    }

    static Runnable getCounter(Player player, VIPData vip) {
        var data = vip.getCountData();
        return () -> {
            if(!player.isOnline() || !IsInVIP(player.getLocation(), vip)) {
                vip.getCountData().get(player.getUniqueId()).getTask().cancel();
                vip.getCountData().get(player.getUniqueId()).setTask(null);
                return;
            }
            if(data.get(player.getUniqueId()).getVipPeriodTime() == null) return;
            if(data.get(player.getUniqueId()).getVipPeriodTime() <= 0) {
                vip.getCountData().get(player.getUniqueId()).getTask().cancel();
                vip.getCountData().get(player.getUniqueId()).setTask(null);
                vip.getCountData().remove(player.getUniqueId());
                player.sendMessage("-------------------------------------------");
                player.sendMessage(toColor("&c" + vip.getVipName() + " の期限が切れました！このVIPのサービスは使用できなくなります。"));
                player.sendMessage(toColor("&aもう一度使用するにはもう一度購入しなおしてください。"));
                player.sendMessage("-------------------------------------------");
                return;
            }
            data.get(player.getUniqueId()).setVipPeriodTime( data.get(player.getUniqueId()).getVipPeriodTime()-1 );
            UpdateShowBar(player, vip);
        };
    }


    public static void ApplySelectorForVIP(Player player, VIPData vip) {
        var selectorData = playerSelectorData.get(player);
        if(selectorData == null) {
            player.sendMessage(toColor("&c選択範囲が正しく指定されていません。"));
            player.closeInventory();
            return;
        }
        if(selectorData.getStart() != null && selectorData.getEnd() != null) {
            vip.setSelectorData(selectorData.clone());
            player.sendMessage(toColor("&a範囲を決定しました。"));
            player.closeInventory();
            return;
        }
        player.sendMessage(toColor("&c選択範囲が正しく指定されていません。"));
    }

    public static boolean IsInVIP(Location location, VIPData vip) {
        if(vip.getSelectorData() == null) {
            return false;
        }
        var start = vip.getSelectorData().getStart();
        var end = vip.getSelectorData().getEnd();

        // startとendの値を正しく設定する
        var minX = Math.min(start.getBlockX(), end.getBlockX());
        var maxX = Math.max(start.getBlockX(), end.getBlockX());
        var minY = Math.min(start.getBlockY(), end.getBlockY());
        var maxY = Math.max(start.getBlockY(), end.getBlockY());
        var minZ = Math.min(start.getBlockZ(), end.getBlockZ());
        var maxZ = Math.max(start.getBlockZ(), end.getBlockZ());


        if (minX <= (int)location.x() && (int)location.x() <= maxX &&
                minY <= (int)location.y() && (int)location.y() <= maxY &&
                minZ <= (int)location.z() && (int)location.z() <= maxZ) {
            return true;
        }
        return false;
    }

}
