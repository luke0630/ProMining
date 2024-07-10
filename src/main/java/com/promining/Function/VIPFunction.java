package com.promining.Function;

import com.promining.Data.VIPData;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import static com.promining.Data.Data.*;

public class VIPFunction {
    public static void RemoveVIP(VIPData data) {
        vipData.remove(data);
    }
    public static void AddVIPMiningBlock(Block block) {
    }
}
