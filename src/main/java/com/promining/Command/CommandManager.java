package com.promining.Command;

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

import java.util.List;

import static com.promining.Data.Data.*;
import static com.promining.Function.Selector.giveWand;
import static com.promining.Function.VIPFunction.IsInVIP;
import static com.promining.Function.VIPFunction.JoinVIP;
import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.GUI.GUIManager.openListGUI;
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
                case "markmode" -> {
                    if(markingPlayer.containsKey(player)) {
                        markingPlayer.remove(player);
                        player.sendMessage(toColor("&cマークモードから抜けました。"));
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
        return List.of("mark", "markmode", "wand");
    }
}
