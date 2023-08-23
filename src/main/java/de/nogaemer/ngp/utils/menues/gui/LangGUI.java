package de.nogaemer.ngp.utils.menues.gui;

import de.nogaemer.ngp.utils.msg.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class LangGUI implements Listener {
    private static HashMap<String, Inventory> invs = new HashMap<String, Inventory>();

    public static void setup(){
        invs.put("EN_US", createInv("EN_US"));
        invs.put("DE_DE", createInv("DE_DE"));
    }


    private static Inventory createInv(String language){
        Inventory inv = Bukkit.createInventory(null , 9*5, Lang.msg("menus.language.select.headline", language).toString());

        Gui.fillInv(inv, Material.LIGHT_GRAY_STAINED_GLASS_PANE);

        ItemStack en_us = Gui.getSkull(Lang.msg("menus.language.select.items.english.name", language).toString() , (List) Lang.msg("menus.language.select.items.english.lore", language), "en_en", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q5MTQ1Njg3N2Y1NGJmMWFjZTI1MWU0Y2VlNDBkYmE1OTdkMmNjNDAzNjJjYjhmNGVkNzExZTUwYjBiZTViMyJ9fX0=");
        ItemStack de_de = Gui.getSkull(Lang.msg("menus.language.select.items.german.name", language).toString() , (List) Lang.msg("menus.language.select.items.german.lore", language), "de_de", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ==");


        inv.setItem(21, en_us);
        inv.setItem(23, de_de);
        inv.setItem(36, Gui.setName(new ItemStack(Material.DARK_OAK_DOOR), "back", Lang.msg("menus.items.back.name", language).toString(), (List) Lang.msg("menus.items.back.lore", language)));

        return inv;
    }

    public static HashMap getInvs() {
        return invs;
    }



    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {

        if (LangGUI.getInvs().containsValue(event.getClickedInventory())){
            Player p = (Player) event.getWhoClicked();

            event.setCancelled(true);

            switch (event.getCurrentItem().getItemMeta().getLocalizedName()){
                case "en_en":
                    Lang.changeLang(p, "en_en");
                    Gui.switchInventory(event.getClickedInventory(), (Inventory) OverviewGUI.getInvs().get(Lang.getLangName(event.getWhoClicked())), p, "down");
                    break;
                case "de_de":
                    Lang.changeLang(p, "de_de");
                    Gui.switchInventory(event.getClickedInventory(), (Inventory) OverviewGUI.getInvs().get(Lang.getLangName(event.getWhoClicked())), p, "down");
                    break;
                case "back":
                    Gui.switchInventory(event.getClickedInventory(), (Inventory) OverviewGUI.getInvs().get(Lang.getLangName(event.getWhoClicked())), p, "down");
                    break;
            }
        }
    }
}
