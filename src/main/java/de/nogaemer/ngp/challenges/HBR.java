package de.nogaemer.ngp.challenges;

import de.nogaemer.ngp.Main;
import de.nogaemer.ngp.utils.Timer;
import de.nogaemer.ngp.utils.copystuff.AnvilGUI;
import de.nogaemer.ngp.utils.menues.gui.Gui;
import de.nogaemer.ngp.utils.menues.gui.challenges.ChallengeGUI;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

//Hotbar randomizer
public class HBR implements Listener {
    private static int endtime = 5 * 60;
    public static boolean running = false;
    private static ArrayList<Material> allItems = new ArrayList<Material>();

    private static HashMap<String, Inventory> invs = new HashMap<String, Inventory>();
    private static HashMap<String, AnvilGUI.Builder> anvilInvs = new HashMap<String, AnvilGUI.Builder>();

    private static HashMap<String, ItemStack> reditem = new HashMap<String, ItemStack>();
    private static HashMap<String, ItemStack> greenitem = new HashMap<String, ItemStack>();

    public static void setup(){
        reditem.put("EN_US", Gui.setName(new ItemStack(Material.RED_CONCRETE), "item", (String) Lang.msg("menus.items.deactivated.name", "EN_US"), (List) Lang.msg("menus.items.deactivated.lore", "EN_US")));
        reditem.put("DE_DE", Gui.setName(new ItemStack(Material.RED_CONCRETE), "item", (String) Lang.msg("menus.items.deactivated.name", "DE_DE"), (List) Lang.msg("menus.items.deactivated.lore", "DE_DE")));
        greenitem.put("EN_US", Gui.setName(new ItemStack(Material.LIME_CONCRETE), "item", (String) Lang.msg("menus.items.activated.name", "EN_US"), (List) Lang.msg("menus.items.activated.lore", "EN_US")));
        greenitem.put("DE_DE", Gui.setName(new ItemStack(Material.LIME_CONCRETE), "item", (String) Lang.msg("menus.items.activated.name", "DE_DE"), (List) Lang.msg("menus.items.activated.lore", "DE_DE")));


        invs.put("EN_US", createInv("EN_US"));
        invs.put("DE_DE", createInv("DE_DE"));

        anvilInvs.put("EN_US", createAnvilInv("EN_US"));
        anvilInvs.put("DE_DE", createAnvilInv("DE_DE"));

        for (Material value : Material.values()) {
            if (value.isItem()){
                allItems.add(value);
            }
        }

    }

    public static void start(){
    }

    public static void stop(){
    }

    public static void reset(){
    }

    public static void task(Integer time){
        if (running && time % endtime == 0){
            setInv();
        }
    }

    public static void setRunning(boolean running) {
        if (running) {
            invs.forEach((s, inventory) -> inventory.setItem(31, greenitem.get(s)));
            ChallengeGUI.switchRunning("hbr", true);
            HBR.running = true;
        } else {
            invs.forEach((s, inventory) -> inventory.setItem(31, reditem.get(s)));
            ChallengeGUI.switchRunning("hbr", false);
            HBR.running = false;
        }
    }

    public static void setInv(){
        if (running){
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getInventory().clear();

                //rand slots
                for (int i = 0; i < 9; i++) {
                    p.getInventory().setItem(i, getRandItem());
                }

                //else slots
                ItemStack item = new ItemStack(Material.BARRIER, 1);
                ItemMeta m = item.getItemMeta();
                m.setDisplayName(ChatColor.RED + "Nicht benutzbarer slot");
                item.setItemMeta(m);

                for (int i = 9; i < 36; i++) {
                    p.getInventory().setItem(i, item);
                }
            }
        }

    }

    public static ItemStack getRandItem(){
        Material m = allItems.get(new Random().nextInt(allItems.size()));
        int r = new Random().nextInt(m.getMaxStackSize());
        ItemStack item;
        if (r == 0){
            item = new ItemStack(m, 1);
        } else{
            item = new ItemStack(m, r);
        }
        return item;
    }

    /**
     * *#############################################################################
     *  +                                 INVENTORYS SECTION
     * ##############################################################################
     */
    private static Inventory createInv(String language){
        Inventory inv = Bukkit.createInventory(null, 5*9, (String) Lang.msg("menus.challenge.invs.hbr.headline", language));

        for (int i = 0; i <= 8; i++) {
            inv.setItem(i, Gui.setName(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), "", " "));
        }
        for (int i = 9; i <= 44; i++) {
            inv.setItem(i, Gui.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), "", " "));
        }

        inv.setItem(31, reditem.get(language));
        inv.setItem(22,Gui.setName(new ItemStack(Material.BEDROCK), "item", "§6§l Hotbar randomizer"));
        inv.setItem(36, Gui.setName(new ItemStack(Material.DARK_OAK_DOOR), "back", Lang.msg("menus.items.back.name", language).toString(), (List) Lang.msg("menus.items.back.lore", language)));
        inv.setItem(4, Gui.setName(new ItemStack(Material.CLOCK), "set", Lang.msg("menus.timer.items.set.name", language).toString(), (List) Lang.msg("menus.timer.items.set.lore", language)));

        return inv;
    }


    private static AnvilGUI.Builder createAnvilInv(String language){
        AnvilGUI.Builder builder = new AnvilGUI.Builder();

        builder.plugin(Main.getInstance());
        builder.itemLeft(new ItemStack(Material.CLOCK));
        builder.title((String) Lang.msg("menus.timer.anvil.title", language));
        builder.text((String) Lang.msg("menus.timer.anvil.placeholder", language));
        builder.onComplete((player, text) -> {
            try {
                if (text.contains(":") &text.length() <=5) {
                    String a = text.split("\\:")[0];
                    String b = text.split("\\:")[1];
                    endtime = (Integer.parseInt(a)*60) + Integer.parseInt(b);
                } else if (text.contains(":") & text.length() <=8){
                    String a = text.split("\\:")[0];
                    String b = text.split("\\:")[1];
                    String c = text.split("\\:")[2];
                    endtime = (Integer.parseInt(a)*3600) + (Integer.parseInt(b)*60) + Integer.parseInt(c);
                } else if (text.contains(":") & text.length() <=11){
                    String s = text.split("\\:")[0];
                    String m = text.split("\\:")[1];
                    String h = text.split("\\:")[2];
                    String d = text.split("\\:")[2];
                    endtime = (Integer.parseInt(m)*86400) + (Integer.parseInt(m)*3600) + (Integer.parseInt(h)*60) + Integer.parseInt(d);
                } else endtime = Integer.parseInt(text); AnvilGUI.Response.close();

                invs.forEach((s, inventory) -> {
                    ItemMeta m = inventory.getItem(3).getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.addAll((List) Lang.msg("menus.timer.items.set.lore", s));
                    lore.add("");
                    lore.add(Lang.msg("menus.challenge.invs.force_battle.endtime", s) + text);
                    m.setLore(lore);
                    inventory.getItem(3).setItemMeta(m);

                });
            } catch (NumberFormatException e) {
                return AnvilGUI.Response.text((String) Lang.msg("menus.timer.anvil.error", language));
            }
            player.openInventory(invs.get(Lang.getLangName(player)));
            return AnvilGUI.Response.close();
        });

        return builder;
    }

    public static HashMap<String, Inventory> getInvs() {
        return invs;
    }


    /**
     * ##############################################################################
     * LISTENER SECTION
     * ##############################################################################
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {

        if (invs.containsValue(event.getClickedInventory())){
            Player p = (Player) event.getWhoClicked();

            event.setCancelled(true);
            switch (event.getCurrentItem().getItemMeta().getLocalizedName()){
                case "item":
                    setRunning(!running);
                    break;
                case "set":
                    anvilInvs.get(Lang.getLangName(p)).open(p);
                    break;
                case "back":
                    Gui.switchInventory(event.getClickedInventory(), (Inventory) ChallengeGUI.getInvs().get(Lang.getLangName(event.getWhoClicked())), p, "down");
                    break;
            }
        }

        if (event.getCurrentItem() == null){
           // event.setCancelled(true);
        }else {
            if (running && Timer.isRunning() && event.getCurrentItem().getType() == Material.BARRIER){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (running && Timer.isRunning()){
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event) {
    }
}
