package de.nogaemer.ngp.utils.menues.gui;

import de.nogaemer.ngp.challenges.Randomizer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class InvByList {
    ArrayList<Inventory> invs = new ArrayList<>();
    ArrayList<ItemStack> items = new ArrayList<>();

    public InvByList() {
    }

    public InvByList(ArrayList<ItemStack> items) {
        if (items != null){
            createInvs(items);
        }
    }

    public void addItems(ArrayList<ItemStack> items){
        if (items != null){
            createInvs(items);
        }
    }

    public void setItems(ArrayList<ItemStack> items){
        clear();
        createInvs(items);
    }

    private void createInvs(ArrayList<ItemStack> items){
        invs.clear();
        this.items.addAll(items);
        for (int i = 0; i < this.items.size(); i++) {
            if (invs.size() <= i/28){
                invs.add(Bukkit.createInventory(null, 5*9, "§9§lPage: " + String.valueOf((i/28) + 1)));
                Gui.fillInv(invs.get(i/28), Material.GRAY_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS_PANE);
                invs.get(i/28).setItem(18, Gui.setName(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "last", "<-"));
                invs.get(i/28).setItem(26, Gui.setName(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "next", "->"));
            }
            invs.get(i/28).setItem((i % 28 / 7 * 7 + i % 28 % 7 + 10 + i % 28 / 7 * 2), this.items.get(i));
        }
    }

    public ArrayList<Inventory> getInvs() {
        return invs;
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
        createInvs(items);
    }
}
