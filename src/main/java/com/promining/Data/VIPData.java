package com.promining.Data;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class VIPData implements Cloneable{

    //****************保存する変数たち******************
    private List<Block> blockList = new ArrayList<>();

    public Material getVipIcon() {
        return vipIcon;
    }

    public void setVipIcon(Material vipIcon) {
        this.vipIcon = vipIcon;
    }

    private Material vipIcon = Material.GOLD_BLOCK;
    private String vipName = "無題のVIP";
    private Integer needYen = 100;
    private Data.SelectorData selectorData = null;
    private  Map<UUID, Data.CountData> countData = new HashMap<>();


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description = "";
    //*************************************************


    final Integer HOUR = 60;
    private Integer periodPerMinute = HOUR; //

    public Data.SelectorData getSelectorData() {
        return selectorData;
    }

    public void setSelectorData(Data.SelectorData selectorData) {
        this.selectorData = selectorData;
    }

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
