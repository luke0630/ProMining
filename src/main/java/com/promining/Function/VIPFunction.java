package com.promining.Function;

import com.promining.Data.Data;
import com.promining.Data.VIPData;
import com.promining.GUI.GUIManager;
import com.promining.ProMining;
import com.promining.Useful;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.promining.Data.Data.*;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.GUI.GUIManager.openListGUI;
import static com.promining.Listening.Listener.isVIPMarkedBlock;
import static com.promining.ProMining.Save;
import static com.promining.ProMining.getEcon;
import static com.promining.Useful.*;

public class VIPFunction {
    public static void RemoveVIP(VIPData vipData) {
        Data.vipData.remove(vipData);
        Save();
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
        Save();
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
                    Save();
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
        Save();
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
            Bukkit.getScheduler().runTask(ProMining.instance, ProMining::Save);
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
        if(vip.getSelectorData() == null || vip.getSelectorData().getStart() == null || vip.getSelectorData().getEnd() == null) {
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

    public static ItemStack getVIPInfoItemStack(Player player, VIPData vip) {
        var item = getItem(vip.getVipIcon(), "&6" + vip.getVipName());
        var isJoining = vip.getCountData().containsKey(player.getUniqueId());
        String isJoiningString = "&8現在、加入していません。";
        if(isJoining) {
            isJoiningString = "&a&l加入しています。";
        }
        setLore(item, List.of(
                isJoiningString,
                "&f&l-------------------------------------",
                "&cVIP期限: " + Useful.getTidyTime( Useful.getHourFromMinute(vip.getPeriodPerMinute()) ),
                "&a加入金: " + vip.getNeedYen() + "円",
                "&c説明: " + vip.getDescription(),
                "&f&l-------------------------------------",
                "&cクリックして掘られるブロック一覧&加入画面へ移動",
                "&f&l-------------------------------------",
                "&6VIP期限とは: このVIPでいられる期間のことで、",
                "&6&l期間が過ぎると&c&lVIPのブロックは手に入れられなくなります。",
                "&6もう一度加入すればまた掘られるようになります。",
                "&6&l期限は対象のVIPエリアにいる間は期限が迫っていきます。"
        ));
        return item;
    }


    public static VIPData getVipDataFromName(String name) {
        for(var vip : vipData) {
            if(vip.getVipName().equalsIgnoreCase(name)) {
                return vip;
            }
        }
        return null;
    }

    public static void addNewBlock(Player player, VIPData data, boolean isMarkMode) {
        getClickedBlock(player, (Object o) -> {
            if(o instanceof Block block) {
                for(var markedBlock : markedBlockList) {
                    if(markedBlock.getLocation().equals(block.getLocation())) {
                        player.sendMessage(toColor("&cそのブロックはすでに登録済みです。"));
                        addNewBlock(player, data, true);
                        return;
                    } else if(markedBlock.getType().equals(block.getType())) {
                        player.sendMessage(toColor("&cそのブロックタイプはすでにノーマルに存在するため追加できませんでした。"));
                        addNewBlock(player, data, true);
                        return;
                    }
                }
                var vip = isVIPMarkedBlock(block.getLocation());
                if(vip != null) {
                    player.sendMessage(toColor("&cそのブロックはすでに&6" + vip.getVipName() + "&cのブロックになっています。"));
                    addNewBlock(player, data, true);
                    return;
                }
                for(var vipData : Data.vipData) {
                    for(var blockData : vipData.getBlockList()) {
                        if(block.getType().equals(blockData.getType())) {
                            player.sendMessage(toColor("&cそのブロックタイプはすでに&6" + vipData.getVipName() +"&cに存在するため追加できませんでした。"));
                            addNewBlock(player, data, true);
                            return;
                        }
                    }
                }
                data.getBlockList().add(block);
                player.sendMessage("&c&lVIPブロックを追加しました");
                if(!isMarkMode) {
                    openListGUI(player, GUIManager.ListGUI.VIP_BLOCK_LIST);
                } else {
                    addNewBlock(player, data, true);
                }
            }
        });
        if(!vipMarkingPlayer.contains(player)) {
            player.sendMessage(toColor("&cVIPマイニング対象にしたいブロックをクリックしてください。"));
            player.closeInventory();
            vipMarkingPlayer.add(player);
        }
    }

}
