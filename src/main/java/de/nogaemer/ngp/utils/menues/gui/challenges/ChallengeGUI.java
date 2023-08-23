package de.nogaemer.ngp.utils.menues.gui.challenges;

import de.nogaemer.ngp.utils.menues.gui.Gui;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ChallengeGUI {
    private static HashMap<String, ItemStack> nameItemEN_US = new HashMap<>();
    private static HashMap<String, ItemStack> nameItemDE_DE = new HashMap<>();
    public static HashMap<String, ItemStack> red = new HashMap<String, ItemStack>();
    public static HashMap<String, ItemStack> green = new HashMap<String, ItemStack>();

    public static HashMap<String, HashMap<String, Inventory>> nameInv = new HashMap<>();
    public static HashMap<Integer, String> loc = new HashMap<>();
    private static HashMap<Integer, HashMap<String, Inventory>> pages = new HashMap<>();

    public static void addItem(Material mat, String locName, String dispName){
        ItemStack itemEN_US = Gui.setName(new ItemStack(mat), locName, (String) Lang.msg(dispName, "EN_US"));
        ItemStack itemDE_DE = Gui.setName(new ItemStack(mat), locName, (String) Lang.msg(dispName, "DE_DE"));
        nameItemEN_US.put(locName, itemEN_US);
        nameItemDE_DE.put(locName, itemDE_DE);
        loc.put(loc.size(), locName);
    }

    public static void addItem(Material mat, String locName, String dispName, HashMap<String, Inventory> inv){
        ItemStack itemEN_US = Gui.setName(new ItemStack(mat), locName, (String) Lang.msg(dispName, "EN_US"));
        ItemStack itemDE_DE = Gui.setName(new ItemStack(mat), locName, (String) Lang.msg(dispName, "DE_DE"));
        nameItemEN_US.put(locName, itemEN_US);
        nameItemDE_DE.put(locName, itemDE_DE);
        nameInv.put(locName, inv);
        loc.put(loc.size(), locName);
    }

    public static void switchRunning(String locName, boolean running){
        int page = 0, slot = 0;

        for (int i = 0; i < nameItemEN_US.values().size(); i++) {
            if (loc.get(i).equalsIgnoreCase(locName)){
                page = i / 7;
                slot = (i%7) + 28;
            }
        }

        if (running) {
            int finalSlot = slot;
            ChallengeGUI.getPages().get(page).forEach((s, inventory) -> inventory.setItem(finalSlot, ChallengeGUI.green.get(s)));
        } else {
            int finalSlot1 = slot;
            ChallengeGUI.getPages().get(page).forEach((s, inventory) -> inventory.setItem(finalSlot1, ChallengeGUI.red.get(s)));
        }
    }

    public static void setup(){
        pages.clear();

        red.put("EN_US", Gui.setName(new ItemStack(Material.RED_CONCRETE), "red", (String) Lang.msg("menus.items.deactivated.name", "EN_US"), (List) Lang.msg("menus.items.deactivated.lore", "EN_US")));
        red.put("DE_DE", Gui.setName(new ItemStack(Material.RED_CONCRETE), "red", (String) Lang.msg("menus.items.deactivated.name", "DE_DE"), (List) Lang.msg("menus.items.deactivated.lore", "DE_DE")));
        green.put("EN_US", Gui.setName(new ItemStack(Material.LIME_CONCRETE), "green", (String) Lang.msg("menus.items.activated.name", "EN_US"), (List) Lang.msg("menus.items.activated.lore", "EN_US")));
        green.put("DE_DE", Gui.setName(new ItemStack(Material.LIME_CONCRETE), "green", (String) Lang.msg("menus.items.activated.name", "DE_DE"), (List) Lang.msg("menus.items.activated.lore", "DE_DE")));

        for (int i = 0; i < nameItemEN_US.values().size(); i++) {
            if (i%7 == 0){
                HashMap<String, Inventory> invs = new HashMap();
                invs.put("EN_US", createInv(pages.size(), "EN_US"));
                invs.put("DE_DE", createInv(pages.size(), "DE_DE"));
                pages.put(pages.size(), invs);
            }

            HashMap<String, Inventory> invs = pages.get(i/7);
            invs.get("EN_US").setItem(i % 7 + 19, nameItemEN_US.get(loc.get(i)));
            invs.get("EN_US").setItem(i % 7 + 19 + 9, red.get("EN_US"));
            invs.get("DE_DE").setItem(i % 7 + 19, nameItemDE_DE.get(loc.get(i)));
            invs.get("DE_DE").setItem(i % 7 + 19 + 9, red.get("DE_DE"));

        }
    }

    public static Inventory createInv(Integer page, String language){
        Inventory inv = Bukkit.createInventory(null, 5*9, Lang.msg("menus.challenge.overview.headline", language).toString() + (page+1));

        for (int i = 0; i <= 8; i++) {
            inv.setItem(i, Gui.setName(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), "", " "));
        }
        for (int i = 9; i <= 44; i++) {
            inv.setItem(i, Gui.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), "", " "));
        }
        for (int i = 19; i < 19+7; i++) {
            inv.setItem(i, new ItemStack(Material.AIR));
        }
        for (int i = 28; i < 28+7; i++) {
            inv.setItem(i, new ItemStack(Material.AIR));
        }
        inv.setItem(18,Gui.setName(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "left", "<--"));
        inv.setItem(26,Gui.setName(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "right", "-->"));

        return inv;
    }

    /*
    public static void switchItem(String locName){
        pages.forEach((integer, stringInventoryHashMap) -> {
            for (int i = 8; i < stringInventoryHashMap.get("EN_US").getSize(); i++) {
                ItemStack item = stringInventoryHashMap.get("EN_US").getItem(i);
                if (item != null && item.getItemMeta() != null) {
                    System.out.println(stringInventoryHashMap.get("EN_US").getItem(i - 9));
                    if (stringInventoryHashMap.get("EN_US").getItem(i - 9).getItemMeta().getLocalizedName().equalsIgnoreCase(locName)){
                        if (item.getType() == Material.LIME_DYE){
                            item.setItemMeta(red.get("EN_US").getItemMeta());
                            item.setType(red.get("EN_US").getType());
                        } else if (item.getType() == Material.RED_DYE){
                            item.setItemMeta(green.get("EN_US").getItemMeta());
                            item.setType(green.get("EN_US").getType());
                        }
                    }
                }
            }
            for (int i = 8; i < stringInventoryHashMap.get("DE_DE").getSize(); i++) {
                ItemStack item = stringInventoryHashMap.get("DE_DE").getItem(i);
                if (item != null && item.getItemMeta() != null) {
                    if (stringInventoryHashMap.get("DE_DE").getItem(i - 9).getItemMeta().getLocalizedName().equalsIgnoreCase(locName)){
                        if (item.getType() == Material.LIME_DYE){
                            item.setItemMeta(red.get("DE_DE").getItemMeta());
                            item.setType(red.get("DE_DE").getType());
                        } else if (item.getType() == Material.RED_DYE){
                            item.setItemMeta(green.get("DE_DE").getItemMeta());
                            item.setType(green.get("DE_DE").getType());
                        }
                    }
                }
            }
        });
    }*/

    public static HashMap getInvs() {
        return pages.get(0);
    }

    public static HashMap<Integer, HashMap<String, Inventory>> getPages() {
        return pages;
    }
}
