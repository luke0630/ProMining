package com.promining.Command;

import com.google.common.base.Strings;
import com.promining.Data.Data;
import com.promining.GUI.GUIManager;
import com.promining.VillagerScript.VillagerClass;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.promining.Data.Data.*;
import static com.promining.Function.Selector.giveWand;
import static com.promining.Function.VIPFunction.*;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.GUI.GUIManager.openListGUI;
import static com.promining.ProMining.Save;
import static com.promining.Useful.toColor;

public class CommandManager implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) return false;
        if(!player.hasPermission("promining")) return false;
        if(!command.getName().equalsIgnoreCase("mining")) return false;

        if(strings.length == 0) {
            openGUI(player, GUIManager.GUI.MAIN_MENU);
        }
        if(strings.length >= 1) {
            switch(strings[0]) {
                case "wand" -> {
                    giveWand(player);
                }
                case "spawn" -> {
                    if(VillagerClass.SpawnVillager(player.getLocation())) {
                        player.sendMessage("&a&lショップを召喚しました。");
                    } else {
                        player.sendMessage("&c&lショップがすでに存在します。");
                    }
                }
                case "test" -> {
                    openListGUI(player, GUIManager.ListGUI.VIP_JOIN);
                    //JoinVIP(vipData.get(Integer.parseInt(strings[1])), player);
                    return false;
                }
                case "mark" -> {
                    markingPlayer.put(player, false);
                    player.sendMessage(toColor("&cマイニング対象にしたいブロックをクリックしてください。"));
                    return false;
                }
                case "movevillager" -> {
                    VillagerData.getEntityData().teleport(player.getLocation());
                    player.sendMessage(toColor("&a&l現在いる場所に村人を移動しました。"));
                    Save();
                    return false;
                }
                case "markmode" -> {
                    if(markingPlayer.containsKey(player) || vipMarkingPlayer.contains(player)) {
                        markingPlayer.remove(player);
                        vipMarkingPlayer.remove(player);
                        player.sendMessage(toColor("&cマークモードから抜けました。"));
                        return false;
                    }
                    if(strings.length >= 2) {
                        var gotData = getVipDataFromName(strings[1]);
                        if(gotData != null) {
                            addNewBlock(player, gotData, true);
                        } else {
                            player.sendMessage(toColor("&cそのVIPは存在しません。"));
                        }
                        return false;
                    }

                    markingPlayer.put(player, true);
                    player.sendMessage(toColor("&cマイニング対象にしたいブロックをクリックしてください。"));
                    return false;
                }
            }
            player.sendMessage(toColor("&cそのコマンドは存在しないです。"));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!command.getName().equalsIgnoreCase("mining")) return null;
        if(!(commandSender instanceof Player player)) return null;
        switch(strings.length) {
            case 1 -> {
                return onTidyList(List.of("mark", "markmode", "wand", "movevillager", "spawn"), strings[0]);
            }
            case 2 -> {
                if(strings[0].equalsIgnoreCase("markmode")) {
                    var vipNameList = new ArrayList<String>();
                    for(var vip : vipData) {
                        vipNameList.add(vip.getVipName());
                    }
                    return onTidyList(vipNameList, strings[1]);
                }
            }
        }
        return null;
    }

    public List<String> onTidyList(List<String> list, String tab) {
        var result = new ArrayList<String>();
        for(var st : list) {
            if(st.startsWith(tab)) {
                result.add(st);
            }
        }
        return result;
    }
}
