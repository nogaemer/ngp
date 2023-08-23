package de.nogaemer.ngp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InvSee implements CommandExecutor, Listener {
    private static ArrayList<Inventory> invs = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1){
            Player player = (Player) sender;
            if (sender instanceof Player) {
                Player pp = Bukkit.getPlayer(args[0]);
                if(pp == null) {
                    sender.sendMessage(ChatColor.RED + "Player doesn't exist");
                    return true;
                } else {
                    Inventory inv = Bukkit.createInventory(null, 9*6);
                    for (int i = 0; i < inv.getContents().length; i++) {
                        inv.setItem(i , new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                    }

                    for (int i = 0; i < pp.getInventory().getStorageContents().length; i++) {
                        inv.setItem(i + 9*2, pp.getInventory().getItem(i));
                    }
                    inv.setItem(2, pp.getInventory().getHelmet());
                    inv.setItem(3, pp.getInventory().getChestplate());
                    inv.setItem(4, pp.getInventory().getLeggings());
                    inv.setItem(5, pp.getInventory().getBoots());
                    inv.setItem(6, pp.getInventory().getItemInOffHand());
                    invs.add(inv);
                    ((Player) sender).openInventory(inv);
                }
            }
        } else sender.sendMessage(ChatColor.RED + "Usage: /invsee <Playername>");
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (invs.contains(event.getInventory())){
            invs.remove(event.getInventory());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (invs.contains(event.getInventory())){
            event.setCancelled(true);
        }
    }
}
