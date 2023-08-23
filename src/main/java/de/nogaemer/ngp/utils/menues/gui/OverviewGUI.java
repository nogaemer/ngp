package de.nogaemer.ngp.utils.menues.gui;

import de.nogaemer.ngp.Main;
import de.nogaemer.ngp.utils.Timer;
import de.nogaemer.ngp.utils.menues.gui.challenges.ChallengeGUI;
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

public class OverviewGUI implements Listener {
    private static HashMap<String, Inventory> invs = new HashMap<String, Inventory>();

    public static void setup(){
        invs.put("EN_US", createInv("EN_US"));
        invs.put("DE_DE", createInv("DE_DE"));
    }


    private static Inventory createInv(String language){
        Inventory inv = Bukkit.createInventory(null , 9*5, Lang.msg("menus.menu.headline", language).toString());

        Gui.fillInv(inv, Material.GRAY_STAINED_GLASS_PANE);

        ItemStack lang = Gui.getSkull(Lang.msg("menus.menu.items.lang.name", language).toString() , (List) Lang.msg("menus.menu.items.lang.lore", language), "lang", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5MTk2YjMzMGM2Yjg5NjJmMjNhZDU2MjdmYjZlY2NlNDcyZWFmNWM5ZDQ0Zjc5MWY2NzA5YzdkMGY0ZGVjZSJ9fX0=");

        inv.setItem(15, Gui.setName(new ItemStack(Material.CHEST_MINECART), "challenges", (String) Lang.msg("menus.menu.items.challenges.name", language), (List) Lang.msg("menus.menu.items.challenges.lore", language)));
        inv.setItem(13, lang);
        inv.setItem(11, Gui.setName(new ItemStack(Material.CLOCK), "timer", (String) Lang.msg("menus.menu.items.timer.name", language), (List) Lang.msg("menus.menu.items.timer.lore", language)));

        inv.setItem(30, Gui.setName(new ItemStack(Material.BARRIER), "reset", (String) Lang.msg("menus.menu.items.reset.name", language), (List) Lang.msg("menus.menu.items.reset.lore", language)));
        inv.setItem(32, Gui.setName(new ItemStack(Material.BARRIER), "unknown", (String) Lang.msg("menus.menu.items.unknown.name", language)));

        return inv;
    }

    public static HashMap getInvs() {
        return invs;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {

        if (OverviewGUI.getInvs().containsValue(event.getClickedInventory())){
            Player p = (Player) event.getWhoClicked();
            event.setCancelled(true);
            switch (event.getCurrentItem().getItemMeta().getLocalizedName()){
                case "lang":
                    Gui.switchInventory(event.getClickedInventory(), (Inventory) LangGUI.getInvs().get(Lang.getLangName(event.getWhoClicked())), p, "up");
                    break;
                case "timer":
                    Gui.switchInventory(event.getClickedInventory(), (Inventory) Timer.getInvs().get(Lang.getLangName(event.getWhoClicked())), p, "left");
                    break;
                case "challenges":
                    Gui.switchInventory(event.getClickedInventory(), (Inventory) ChallengeGUI.getInvs().get(Lang.getLangName(event.getWhoClicked())), p, "right");
                    break;
                case "reset":
                    Main.getInstance().getConfig().set("isReset", true);
                    Main.getInstance().saveConfig();
                    break;
            }
        }
    }
}
