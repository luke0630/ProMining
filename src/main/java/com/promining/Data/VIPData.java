package com.promining.Data;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class VIPData implements Cloneable{
    private List<Block> blockList = new ArrayList<>();
    private String vipName = "無題のVIP";
    private Integer needYen = 100;


    final Integer HOUR = 60;
    private Integer periodPerMinute = HOUR; //

    private  Map<UUID, Data.CountData> countData = new HashMap<>();

    public Map<UUID, Data.CountData> getCountData() {
        return countData;
    }

    public void setCountData(Map<UUID, Data.CountData> countData) {
        this.countData = countData;
    }

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

    @Override
    public VIPData clone() {
        try {
            VIPData clone = (VIPData) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
