package com.promining.GUI;

import com.promining.System.RunnableSystem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ListGUIAbstract {
    protected Player player;
    public GUIListData getGUIListData(Player player) {
        this.player = player;
        return new GUIListData(
                title(),
                itemList(),
                centerInteractButton(),
                contentCallBack(),
                centerCallBack(),
                backCallBack()
        );
    }

    protected abstract GUIManager.ListGUI guiType();

    @NotNull
    protected abstract String title();

    public abstract List<ItemStack> itemList();

    protected abstract ItemStack centerInteractButton();
    protected abstract RunnableSystem.Runnable contentCallBack();
    protected abstract Runnable centerCallBack();
    protected abstract Runnable backCallBack();
}
