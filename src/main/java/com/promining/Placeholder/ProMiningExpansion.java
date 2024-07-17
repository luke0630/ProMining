package com.promining.Placeholder;

import com.promining.Data.Data;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.promining.Data.Data.breakCounterPlayer;

public class ProMiningExpansion extends PlaceholderExpansion {
    @Override
    @NotNull
    public String getAuthor() {
        return "Luke0630";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "promining";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }


    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if(!(offlinePlayer instanceof Player player)) {
            return "";
        }
        if(params.equalsIgnoreCase("player")) {
            var you = breakCounterPlayer.get(offlinePlayer.getUniqueId());
            if(you != null) {
                return you + "回";
            } else {
                return "0回";
            }
        }
        try {
            String[] lines = params.split("_");
            int rank = Integer.parseInt(lines[0]);
            var rankData = getRankSet(rank);

            if(lines.length == 2 && lines[1].equalsIgnoreCase("name")) {
                var targetPlayer = Bukkit.getOfflinePlayer(rankData.uuid);
                return targetPlayer.getName();
            }
            if(rankData != null) {
                return String.valueOf(rankData.count);
            }
            return "---------";
        } catch(Exception ignored) {
            return "---------";
        }
    }

    public RankSet getRankSet(int targetRank) {
        var map = breakCounterPlayer.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue, (k1, k2) -> k1, LinkedHashMap::new));
        var list = map.entrySet();

        int ranking = 1;
        for(var data : list) {
            if(ranking == targetRank) {
                return new RankSet(data.getKey(), data.getValue());
            }
            ranking++;
        }
        return null;
    }

    public class RankSet {
        public UUID uuid;
        public Long count;

        public RankSet(UUID uuid, Long count) {
            this.uuid = uuid;
            this.count = count;
        }
    }
}
