package de.nogaemer.ngp.utils.msg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ConsolMSG {

    public static void msg(String msg){
        Bukkit.getConsoleSender().sendMessage();
    }

    public static void warning(String msg){
        Bukkit.getServer().getLogger().warning(replaceColor(msg));
    }

    public static void error(String msg){
        Bukkit.getServer().getLogger().severe(replaceColor(msg));
    }

    private static String replaceColor(String msg){
        msg = msg.replace("§0", "");
        msg = msg.replace("§1", "");
        msg = msg.replace("§2", "");
        msg = msg.replace("§3", "");
        msg = msg.replace("§4", "");
        msg = msg.replace("§5", "");
        msg = msg.replace("§6", "");
        msg = msg.replace("§7", "");
        msg = msg.replace("§8", "");
        msg = msg.replace("§9", "");
        msg = msg.replace("§a", "");
        msg = msg.replace("§b", "");
        msg = msg.replace("§c", "");
        msg = msg.replace("§d", "");
        msg = msg.replace("§e", "");
        msg = msg.replace("§f", "");
        msg = msg.replace("§k", "");
        msg = msg.replace("§l", "");
        msg = msg.replace("§m", "");
        msg = msg.replace("§n", "");
        msg = msg.replace("§o", "");
        msg = msg.replace("§r", "");

        return msg;
    }
}
