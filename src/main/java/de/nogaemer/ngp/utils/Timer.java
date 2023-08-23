package de.nogaemer.ngp.utils;

import de.nogaemer.ngp.Main;
import de.nogaemer.ngp.challenges.ForceBattle;
import de.nogaemer.ngp.challenges.HBR;
import de.nogaemer.ngp.utils.copystuff.AnvilGUI;
import de.nogaemer.ngp.utils.menues.gui.Gui;
import de.nogaemer.ngp.utils.menues.gui.OverviewGUI;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class Timer implements Listener {
    private static HashMap<String, Inventory> invs = new HashMap<String, Inventory>();
    private static HashMap<String, AnvilGUI.Builder> anvilInvs = new HashMap<String, AnvilGUI.Builder>();
    private static boolean running;
    private static int time;

    public static void setup(boolean running, int time) {
        Timer.running = running;
        Timer.time = time;
        run();

        invs.put("EN_US", createInv("EN_US"));
        invs.put("DE_DE", createInv("DE_DE"));

        anvilInvs.put("EN_US", createAnvilInv("EN_US"));
        anvilInvs.put("DE_DE", createAnvilInv("DE_DE"));
    }

    private static void run(){
        new BukkitRunnable() {
            @Override
            public void run() {
                task();
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

    private static void task(){
        sendActionBar();
        if (running){
            ForceBattle.end();
            HBR.task(time);

            time++;
        }
    }

    private static void sendActionBar() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isRunning()){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(((String) Lang.msg("message.timer.paused", player))));
                continue;
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,(new TextComponent(getActionBarMSG() + ForceBattle.sendActionbar(player.getUniqueId()))));
        }
    }

    public static String getActionBarMSG(){
        String msg;

        int s = (getTime()%60);
        int m = ((getTime()%3600)/60);
        int h = ((getTime()%86400/3600));
        int d = ((getTime()/86400));
        if (d == 0 && h == 0 && m == 0){
            msg = ChatColor.GOLD.toString() + ChatColor.BOLD + s + "s";
        } else if (d == 0 && h == 0){
            msg = ChatColor.GOLD.toString() + ChatColor.BOLD  + m + "m " + s + "s";
        } else if (d == 0){
            msg = ChatColor.GOLD.toString() + ChatColor.BOLD  + h + "h " + m + "m " + s + "s";
        } else msg = ChatColor.GOLD.toString()  + ChatColor.BOLD + d + "d " + h + "h " + m + "m " + s + "s";

        return msg;
    }

    private static void start(){
        setRunning(true);
        ForceBattle.start();
        HBR.start();
    }

    private static void stop(){
        setRunning(false);
        ForceBattle.stop();
        HBR.stop();
    }

    private static void reset(){
        running = false;
        setTime(0);
        ForceBattle.reset();
        HBR.reset();
    }


    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Timer.running = running;
    }

    public static int getTime() {
        return time;
    }

    public static void setTime(int time) {
        Timer.time = time;
    }

    public static HashMap getInvs() {
        return invs;
    }
    
    
    
    private static Inventory createInv(String language){
        Inventory inv = Bukkit.createInventory(null , 9*5, (String) Lang.msg("menus.timer.headline", language));

        Gui.fillInv(inv, Material.LIGHT_GRAY_STAINED_GLASS_PANE);


        inv.setItem(13, Gui.setName(new ItemStack(Material.OAK_SIGN), "timer.set", Lang.msg("menus.timer.items.set.name", language).toString(), (List) Lang.msg("menus.timer.items.set.lore", language)));
        inv.setItem(21, Gui.setName(new ItemStack(Material.LIME_DYE), "timer.start", (String) Lang.msg("menus.timer.items.start.name", language), (List) Lang.msg("menus.timer.items.start.lore", language)));
        inv.setItem(23, Gui.setName(new ItemStack(Material.RED_DYE), "timer.stop", (String) Lang.msg("menus.timer.items.stop.name", language), (List) Lang.msg("menus.timer.items.stop.lore", language)));
        inv.setItem(31, Gui.setName(new ItemStack(Material.BARRIER), "timer.reset", (String) Lang.msg("menus.timer.items.reset.name", language), (List) Lang.msg("menus.timer.items.reset.lore", language)));
        inv.setItem(44, Gui.setName(new ItemStack(Material.DARK_OAK_DOOR), "timer.back", Lang.msg("menus.items.back.name", language).toString(), (List) Lang.msg("menus.items.back.lore", language)));

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
                    setTime((Integer.parseInt(a)*60) + Integer.parseInt(b));
                } else if (text.contains(":") & text.length() <=8){
                    String a = text.split("\\:")[0];
                    String b = text.split("\\:")[1];
                    String c = text.split("\\:")[2];
                    setTime((Integer.parseInt(a)*3600) + (Integer.parseInt(b)*60) + Integer.parseInt(c));
                } else if (text.contains(":") & text.length() <=11){
                    String s = text.split("\\:")[0];
                    String m = text.split("\\:")[1];
                    String h = text.split("\\:")[2];
                    String d = text.split("\\:")[2];
                    setTime((Integer.parseInt(m)*86400) + (Integer.parseInt(m)*3600) + (Integer.parseInt(h)*60) + Integer.parseInt(d));
                } else setTime(Integer.parseInt(text)); AnvilGUI.Response.close();
            } catch (NumberFormatException e) {
                return AnvilGUI.Response.text((String) Lang.msg("menus.timer.anvil.error", language));
            }
            player.openInventory(invs.get(Lang.getLangName(player)));
            return AnvilGUI.Response.close();
        });

        return builder;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {

        if (Timer.getInvs().containsValue(event.getClickedInventory())){
            Player p = (Player) event.getWhoClicked();

            event.setCancelled(true);
            switch (event.getCurrentItem().getItemMeta().getLocalizedName()){
                case "timer.set":
                    anvilInvs.get(Lang.getLangName(p)).open(p);
                    break;
                case "timer.start":
                    start();
                    break;
                case "timer.stop":
                    stop();
                    break;
                case "timer.reset":
                    reset();
                    break;
                case "timer.back":
                    Gui.switchInventory(event.getClickedInventory(), (Inventory) OverviewGUI.getInvs().get(Lang.getLangName(p)), p, "right");
                    break;
            }
        }
    }
}


