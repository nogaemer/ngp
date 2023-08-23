package de.nogaemer.ngp.utils.menues.gui;

import de.nogaemer.ngp.Main;
import de.nogaemer.ngp.utils.FileManager;
import de.nogaemer.ngp.utils.msg.ConsolMSG;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class Gui implements Listener {
    private static ArrayList<Inventory> switchInv = new ArrayList<>();

    public static HashMap<String, ItemStack> setNames(Material mat, String localizedName, String displayName, String lore){
        String[] lang = {"DE_DE", "EN_US"};
        HashMap<String, ItemStack> items = new HashMap<>();

        for (String s : lang) {
            ItemStack item = new ItemStack(mat);
            ItemMeta m = item.getItemMeta();
            m.setLocalizedName( (String) Lang.msg(localizedName, s));
            m.setDisplayName((String) Lang.msg(displayName, s));
            m.setLore((List) Lang.msg(lore, s));
            item.setItemMeta(m);
            items.put(s, item);
        }

        return items;
    }

    public static HashMap<String, ItemStack> setNames(Material mat, String localizedName, String displayName){
        String[] lang = {"DE_DE", "EN_US"};
        HashMap<String, ItemStack> items = new HashMap<>();

        for (String s : lang) {
            ItemStack item = new ItemStack(mat);
            ItemMeta m = item.getItemMeta();
            m.setLocalizedName( (String) Lang.msg(localizedName, s));
            m.setDisplayName((String) Lang.msg(displayName, s));
            item.setItemMeta(m);
            items.put(s, item);
        }

        return items;
    }

    public static HashMap<String, ItemStack> setNames(Material mat, String localizedName){
        String[] lang = {"DE_DE", "EN_US"};
        HashMap<String, ItemStack> items = new HashMap<>();

        for (String s : lang) {
            ItemStack item = new ItemStack(mat);
            ItemMeta m = item.getItemMeta();
            m.setLocalizedName( (String) Lang.msg(localizedName, s));
            item.setItemMeta(m);
            items.put(s, item);
        }

        return items;
    }

    public static ItemStack setName(ItemStack item, String localizedName, String displayName, List lore){
        ItemMeta m = item.getItemMeta();
        m.setLocalizedName(localizedName);
        m.setDisplayName(displayName);
        m.setLore(lore);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack setName(ItemStack item, String localizedName, String displayName){
        ItemMeta m = item.getItemMeta();
        m.setLocalizedName(localizedName);
        m.setDisplayName(displayName);
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack setName(ItemStack item, String localizedName){
        ItemMeta m = item.getItemMeta();
        m.setLocalizedName(localizedName);
        item.setItemMeta(m);

        return item;
    }

    public static ItemStack getSkull(String name, String texture){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        head = Bukkit.getUnsafe().modifyItemStack(head, "{SkullOwner:{Id:\"" + UUID.randomUUID() + "\",Properties:{textures:[{Value:\""
                + texture + "\"}]}}}");

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        headMeta.setDisplayName(name);

        head.setItemMeta(headMeta);

        return head;
    }

    public static ItemStack getSkull(String name, List description, String localizedName, String texture){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        head = Bukkit.getUnsafe().modifyItemStack(head, "{SkullOwner:{Id:\"" + UUID.randomUUID() + "\",Properties:{textures:[{Value:\""
                + texture + "\"}]}}}");

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        headMeta.setDisplayName(name);
        headMeta.setLore(description);
        headMeta.setLocalizedName(localizedName);

        head.setItemMeta(headMeta);

        return head;
    }

    public static ItemStack getEntityHeads(EntityType entity, String name){
        String texture = FileManager.getCfg().getString("EntityHeads." + entity.name().toLowerCase() + ".textureCode");
        String material = FileManager.getCfg().getString("EntityHeads." + entity.name().toLowerCase() + ".material");

        ItemStack item = new ItemStack(Material.BARRIER);

        if (texture != null && texture != "doesn't exist" && texture != " doesn't exist"){
            item = getSkull(name, texture);
        } else if (Material.getMaterial(material) != null){
            item = new ItemStack(Material.getMaterial(material));
        }

        ItemMeta m = item.getItemMeta();

        m.setDisplayName(name);

        item.setItemMeta(m);

        return item;
    }

    public static ItemStack getEntityHeads(EntityType entity, String name, List description, String localizedName){
        String texture = FileManager.getCfg().getString("EntityHeads." + entity.name().toLowerCase() + ".textureCode");
        String material = FileManager.getCfg().getString("EntityHeads." + entity.name().toLowerCase() + ".material");

        ItemStack item = new ItemStack(Material.BARRIER);

        if (texture != null && texture != "doesn't exist" && texture != " doesn't exist"){
            item = getSkull(name, description, localizedName, texture);
        } else if (Material.getMaterial(material) != null){
            item = new ItemStack(Material.getMaterial(material));
        }

        ItemMeta m = item.getItemMeta();

        m.setDisplayName(name);
        m.setLore(description);
        m.setLocalizedName(localizedName);

        item.setItemMeta(m);

        return item;
    }

    public static void fillInv(Inventory inv, Material fillMat){
        ItemStack glass = new ItemStack(fillMat);
        ItemMeta m = glass.getItemMeta();
        m.setDisplayName(" ");
        glass.setItemMeta(m);
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, glass);
        }
    }

    public static void fillInv(Inventory inv, Material fillMat, Material secondMaterial){
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, Gui.setName(new ItemStack(fillMat), "", " "));
        }
        for (int i = 0; i <= 8; i++) {
            inv.setItem(i, Gui.setName(new ItemStack(secondMaterial), "", " "));
        }
    }

    public static void switchInventory(Inventory now, Inventory then, Player p, String direktion){
        if (now.getSize() == then.getSize()){
            int speed = 2;
            Inventory switchInv = Bukkit.createInventory(null, then.getSize(), "§7» §l§9Switch");
            Gui.switchInv.add(switchInv);
            switchInv.setContents(now.getContents());

            switch (direktion){
                case "down":
                    for (int i = 0; i < then.getSize()+9; i = i+9) {
                        int finalI = i;
                        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                            for (int x = 0; x < now.getSize(); x++) {
                                if (x- finalI >= 0){
                                    switchInv.setItem(x- finalI,now.getItem(x));
                                }
                            }
                            for (int x = 0; x < then.getSize(); x++) {
                                if (x+then.getSize()- finalI <= then.getSize()-1){
                                    switchInv.setItem(x+then.getSize()- finalI,then.getItem(x));
                                }
                            }
                            p.openInventory(switchInv);
                            if (finalI == then.getSize()){
                                p.openInventory(then);
                                switchInv.clear();
                            }
                        }, i / 9 * speed);
                    }
                    break;
                case "left":
                    for (int i = 0; i <= 9; i++) {
                        int finalI = i;
                        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                            for (int x = 0; x < now.getSize(); x++) {
                                if (x/9 == (x+ finalI)/9 && x+ finalI < now.getSize()){
                                    switchInv.setItem(x+ finalI,now.getItem(x));
                                }
                            }
                            for (int x = 0; x < then.getSize(); x++) {
                                if (x/9 == (x-9+ finalI)/9 && x-9+ finalI >= 0){
                                    switchInv.setItem(x-9+ finalI,then.getItem(x));
                                }
                            }
                            p.openInventory(switchInv);
                            if (finalI == 9){
                                p.openInventory(then);
                                switchInv.clear();
                            }
                        }, i * speed);
                    }
                    break;
                case "up":
                    for (int i = 0; i < then.getSize()+9; i = i+9) {
                        int finalI = i;
                        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                            for (int x = 0; x < now.getSize(); x++) {
                                if (x+finalI <= now.getSize()-1){
                                    switchInv.setItem(x+finalI ,now.getItem(x));
                                }
                            }
                            for (int x = 0; x < then.getSize(); x++) {
                                if (x-then.getSize()+finalI >= 0){
                                    switchInv.setItem(x-then.getSize()+finalI ,then.getItem(x));
                                }
                            }
                            p.openInventory(switchInv);
                            if (finalI == then.getSize()){
                                p.openInventory(then);
                                switchInv.clear();
                            }
                        }, i / 9 * speed);
                    }
                    break;
                case "right":
                    for (int i = 0; i <= 9; i++) {
                        int finalI = i;
                        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                            for (int x = 0; x < now.getSize(); x++) {
                                if (x/9 == (x- finalI)/9 && x- finalI >= 0){
                                    switchInv.setItem(x- finalI,now.getItem(x));
                                }
                            }
                            for (int x = 0; x < then.getSize(); x++) {
                                if (x/9 == (x+9- finalI)/9 && x+9- finalI < then.getSize()){
                                    switchInv.setItem(x+9- finalI,then.getItem(x));
                                }
                            }
                            p.openInventory(switchInv);
                            if (finalI == 9){
                                p.openInventory(then);
                                switchInv.clear();
                            }
                        }, i * speed);
                    }
                    break;
                default:
                    ConsolMSG.error("de.nogaemer.ngp.utils.menues.gui.GUI |30|  direktion is wrong");
                    break;
            }
            Gui.switchInv.remove(switchInv);

        } else ConsolMSG.error("de.nogaemer.ngp.utils.menues.gui.GUI |34|  Inventorys has not the same size");
    }

    public static void switchSmallInventory(Inventory now, Inventory then, Player p, String direktion){
        if (now.getSize() == then.getSize()){
            int speed = 2;
            Inventory switchInv = Bukkit.createInventory(null, then.getSize(), "§7» §l§9Switch");
            Gui.switchInv.add(switchInv);
            switchInv.setContents(now.getContents());

            switch (direktion){
                case "left":
                    for (int i = 0; i <= 9; i++) {
                        int finalI = i;
                        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                            for (int x = 9; x < now.getSize(); x++) {
                                if (x/9 == (x+ finalI)/9 && x+ finalI < now.getSize()){
                                    if ((x + finalI + 1) % 9 != 0 && (x + finalI) % 9 != 0 ) {
                                        if ((x + 1) % 9 == 0 || x % 9 == 0){
                                            switchInv.setItem(x + finalI, new ItemStack(Material.AIR));
                                            continue;
                                        }
                                        switchInv.setItem(x+ finalI,now.getItem(x));
                                    }
                                    //switchInv.setItem(x+ finalI,now.getItem(x));
                                }
                            }
                            for (int x = 9; x < then.getSize(); x++) {
                                if (x/9 == (x-9+ finalI)/9 && x-9+ finalI >= 0){
                                    if ((x-9+ finalI + 1 ) % 9 != 0 && (x-9+ finalI + 2) % 9 != 0) {
                                        if ((x + 1) % 9 == 0 ||(x) % 9 == 0){
                                            //switchInv.setItem(x+9- finalI + 2, new ItemStack(Material.AIR));
                                            continue;
                                        }
                                        switchInv.setItem(x-9+ finalI + 1,then.getItem(x));
                                    }
                                    //switchInv.setItem(x-9+ finalI,then.getItem(x));
                                }
                            }
                            p.openInventory(switchInv);
                            if (finalI == 9){
                                p.openInventory(then);
                                switchInv.clear();
                            }
                        }, i * speed);
                    }
                    break;
                case "right":
                    for (int i = 0; i <= 9; i++) {
                        int finalI = i;
                        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                            for (int x = 9; x < now.getSize(); x++) {
                                if (x/9 == (x- finalI)/9 && x- finalI >= 0){
                                    if ((x- finalI + 1) % 9 != 0 && (x- finalI) % 9 != 0 ) {
                                        if ((x + 1) % 9 == 0 || x % 9 == 0){
                                            switchInv.setItem(x- finalI, new ItemStack(Material.AIR));
                                            continue;
                                        }
                                        switchInv.setItem(x- finalI,now.getItem(x));
                                    }
                                }
                            }
                            for (int x = 9; x < then.getSize(); x++) {
                                if (x/9 == (x+9- finalI - 1)/9 && x+9- finalI - 1 < then.getSize()){
                                    if ((x+9- finalI - 1 + 1) % 9 != 0 && (x+9- finalI - 1) % 9 != 0) {
                                        if ((x + 1) % 9 == 0 ||(x) % 9 == 0){
                                            //switchInv.setItem(x+9- finalI - 2, new ItemStack(Material.AIR));
                                            continue;
                                        }
                                        switchInv.setItem(x+9- finalI - 1,then.getItem(x));
                                    }

                                }
                            }
                            p.openInventory(switchInv);
                            if (finalI == 9){
                                p.openInventory(then);
                                switchInv.clear();
                            }
                        }, i * speed);
                    }
                    break;
                default:
                    ConsolMSG.error("de.nogaemer.ngp.utils.menues.gui.GUI |30|  direktion is wrong");
                    break;
            }
            Gui.switchInv.remove(switchInv);

        } else ConsolMSG.error("de.nogaemer.ngp.utils.menues.gui.GUI |34|  Inventorys has not the same size");
    }

    public static String nameToString(String s){
        String string = "";

        s = s.toLowerCase();
        ArrayList<String> strings = new ArrayList<String>(List.of(s.split("_")));
        for (String string1 : strings) {
            char[] arr = string1.toCharArray();
            arr[0] = Character.toUpperCase(arr[0]);
            string = string + String.valueOf(arr) + " ";
        }

        return string;
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (switchInv != null && switchInv.contains(event.getClickedInventory())){
            event.setCancelled(true);
        }
    }

   /* @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryInteractEvent event) {
        if (switchInv != null && event.getInventory() == switchInv){
            event.setCancelled(true);
        }
    }*/

    @EventHandler(ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        if (switchInv != null && switchInv.contains(event.getSource())){
            event.setCancelled(true);
        }
    }
}
