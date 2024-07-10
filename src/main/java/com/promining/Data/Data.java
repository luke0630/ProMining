package com.promining.Data;
import com.promining.GUI.GUIListData;
import com.promining.GUI.GUIManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class Data {

    //GUIデータ
    public static Map<Player, Integer> playerOpenPage = new HashMap<>();
    public static Map<Player, GUIListData> playerOpenListGUIData = new HashMap<>();
    public static Map<Player, GUIManager.GUI> playerOpenGUI = new HashMap<>();
    public static Map<Player, VIPData> playerOpenVipData = new HashMap<>();



    public static List<VIPData> vipData = new ArrayList<>();
    public static Map<UUID, Long> breakCounterPlayer = new TreeMap<>(Collections.reverseOrder());;
    public static Map<Player, Boolean> markingPlayer = new HashMap<>();
    public static List<Block> markedBlockList = new ArrayList<>();
}
