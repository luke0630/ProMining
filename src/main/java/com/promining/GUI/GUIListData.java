package com.promining.GUI;

import com.promining.System.RunnableSystem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.promining.Useful.toColor;

public class GUIListData {
    private String title = "";
    private List<ItemStack> itemList = new ArrayList<>();
    private ItemStack centerInteractButton;
    private RunnableSystem.Runnable contentCallBack;
    private Runnable centerCallBack;
    private Runnable backCallBack;

    public GUIListData(String title, List<ItemStack> itemList, ItemStack centerInteractButton, RunnableSystem.Runnable contentCallBack, Runnable centerCallBack, Runnable backCallBack) {
        this.title = toColor(title);
        this.itemList = itemList;
        this.centerInteractButton = centerInteractButton;
        this.contentCallBack = contentCallBack;
        this.centerCallBack = centerCallBack;
        this.backCallBack = backCallBack;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemStack> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemStack> itemList) {
        this.itemList = itemList;
    }

    public Runnable getCenterCallBack() {
        return centerCallBack;
    }

    public void setCenterCallBack(Runnable centerCallBack) {
        this.centerCallBack = centerCallBack;
    }

    public Runnable getBackCallBack() {
        return backCallBack;
    }

    public void setBackCallBack(Runnable backCallBack) {
        this.backCallBack = backCallBack;
    }

    public ItemStack getCenterInteractButton() {
        return centerInteractButton;
    }

    public void setCenterInteractButton(ItemStack centerInteractButton) {
        this.centerInteractButton = centerInteractButton;
    }

    public RunnableSystem.Runnable getContentCallBack() {
        return contentCallBack;
    }

    public void setContentCallBack(RunnableSystem.Runnable contentCallBack) {
        this.contentCallBack = contentCallBack;
    }
}
