package com.promining.GUI.ListGUIs;

import com.promining.Data.Data;
import com.promining.GUI.GUIManager;
import com.promining.GUI.ListGUIAbstract;
import com.promining.System.RunnableSystem;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import static com.promining.GUI.GUIManager.openGUI;
import static com.promining.Useful.setLore;

public class MaterialList extends ListGUIAbstract {
    @Override
    protected GUIManager.ListGUI guiType() {
        return GUIManager.ListGUI.MATERIAL_LIST;
    }

    @Override
    protected @NotNull String title() {
        return "&c&lブロックを選択";
    }

    @Override
    public List<ItemStack> itemList() {
        var item = new ArrayList<ItemStack>();
        for(var mate : Material.values()) {
            if(mate.isEmpty()) continue;
            if(mate.isAir()) continue;
            var result = new ItemStack(mate);
            setLore(result, List.of(
                    "&cクリックしてアイコンにする"
            ));
            item.add( result );
        }
        return item;
    }

    @Override
    protected ItemStack centerInteractButton() {
        return null;
    }

    @Override
    protected RunnableSystem.Runnable contentCallBack() {
        return (Object object) -> {
            if(object instanceof InventoryClickEvent event) {
                if(event.getCurrentItem() == null) return;
                var currentVip = Data.playerOpenVipData.get(player);

                Material mate = event.getCurrentItem().getType();
                currentVip.setVipIcon(mate);
                openGUI(player, GUIManager.GUI.EDITOR_VIP);
            }
        };
    }

    @Override
    protected Runnable centerCallBack() {
        return null;
    }

    @Override
    protected Runnable backCallBack() {
        return () -> openGUI(player, GUIManager.GUI.EDITOR_VIP);
    }
}
