package com.promining.Function;

import org.bukkit.Location;
import org.bukkit.block.Block;

import static com.promining.Data.Data.markedBlockList;

public class NormalBlockFunction {
    public static boolean RemoveBlock(Location block) {
        for(var data : markedBlockList) {
            if(data.getLocation().equals(block)) {
                markedBlockList.remove(data);
                return true;
            }
        }
        return false;
    }
}
