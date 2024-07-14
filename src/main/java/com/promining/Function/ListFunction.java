package com.promining.Function;

import com.promining.Data.Data;
import org.bukkit.entity.Player;

public class ListFunction {
    public static Integer getMaxPage(Player player) {
        if(Data.playerOpenListGUIData.containsKey(player)) {
            var openData = Data.playerOpenListGUIData.get(player);
            return openData.getItemList().size() / 46;
        }
        return null;
    }
}
