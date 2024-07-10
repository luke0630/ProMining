package com.promining.Data;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class VIPData {
    private List<Block> blockList = new ArrayList<>();
    private String vipName = "無題のVIP";
    private Integer needYen = 100;


    final Integer HOUR = 64;
    private Integer periodPerMinute = HOUR; //

    public List<Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<Block> blockList) {
        this.blockList = blockList;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public Integer getNeedYen() {
        return needYen;
    }

    public void setNeedYen(Integer needYen) {
        this.needYen = needYen;
    }

    public Integer getPeriodPerMinute() {
        return periodPerMinute;
    }

    public void setPeriodPerMinute(Integer periodPerMinute) {
        this.periodPerMinute = periodPerMinute;
    }
}
