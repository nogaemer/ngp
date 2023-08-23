package de.nogaemer.ngp.utils.menues.gui.command;

import de.nogaemer.ngp.utils.menues.gui.OverviewGUI;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class GuiCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        p.openInventory((Inventory) OverviewGUI.getInvs().get(Lang.getLangName(p)));
        return false;
    }
}
