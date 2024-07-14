package com.promining.Data;
import com.promining.GUI.GUIListData;
import com.promining.GUI.GUIManager;
import com.promining.System.RunnableSystem;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

    public class Data {

        //GUIデータ
        public static Map<Player, Integer> playerOpenPage = new HashMap<>();
        public static Map<Player, GUIListData> playerOpenListGUIData = new HashMap<>();
        public static Map<Player, GUIManager.GUI> playerOpenGUI = new HashMap<>();
        public static Map<Player, VIPData> playerOpenVipData = new HashMap<>();



        public static Map<Player, RunnableSystem.Runnable> clickedMiningBlock = new HashMap<>();

        public static Map<Player, BukkitTask> actionBarTaskList = new HashMap<>();


    public static class CountData {
        private BukkitTask task;
        private Long vipPeriodTime = 0L;

        public Long getVipPeriodTime() {
            return vipPeriodTime;
        }

        public void setVipPeriodTime(Long vipPeriodTime) {
            this.vipPeriodTime = vipPeriodTime;
        }

        public CountData(BukkitTask task, Long vipPeriodTime) {
            this.task = task;
            this.vipPeriodTime = vipPeriodTime;
        }


        public BukkitTask getTask() {
            return task;
        }

        public void setTask(BukkitTask task) {
            this.task = task;
        }
    }


    public static List<VIPData> vipData = new ArrayList<>();
    public static Map<UUID, Long> breakCounterPlayer = new TreeMap<>(Collections.reverseOrder());;
    public static Map<Player, Boolean> markingPlayer = new HashMap<>();
    public static List<Block> markedBlockList = new ArrayList<>();
}
