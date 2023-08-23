package de.nogaemer.ngp;

import de.nogaemer.ngp.utils.enums.Chess;
import de.nogaemer.ngp.utils.menues.gui.InvByList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TestCmm implements CommandExecutor, TabCompleter {
    private static HashMap<Player, ItemStack[]> inv = new HashMap<>();
    public static InvByList invs = new InvByList();
    static String test = "WHITE_ROOK:0,7/WHITE_KNIGHT:1,6/WHITE_BISHOP:2,5/WHITE_QUEEN:3/WHITE_KING:4/WHITE_PAWN:8-16/BLACK_ROOK:63,56/BLACK_KNIGHT:62,57/BLACK_BISHOP:61,58/BLACK_QUEEN:59/BLACK_KING:60/BLACK_PAWN:55-48";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        Material matDark = Material.STRIPPED_DARK_OAK_WOOD;
        Material matLight = Material.STRIPPED_BIRCH_WOOD;

        for (int i = 0; i < 8; i++) {
            for (int x = 0; x < 8; x++) {
                boolean isLightSquare = (i + x) % 2 != 1;

                Material material = (isLightSquare) ? matDark : matLight;
                Location loc = p.getLocation().add(i, 0, x);

                loc.getBlock().setType(material);
            }
        }
        decodeFormation(test, p);
        return false;
    }

    public static void msg(String msg){
        Bukkit.getOnlinePlayers().forEach(player ->  player.sendMessage(msg));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> list = new ArrayList<>();
        for (Material value : Material.values()) {
            if (args.length != 0 && value.name().contains(args[args.length - 1]))
                list.add(value.name());
        }
        return list;
    }

    public final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

    public static void decodeFormation(String formation, Player p){
        for (String s : formation.split("/")) {
            int customModelID = Chess.valueOf(s.split(":")[0]).getID();
            String count = s.split(":")[1];
            if (count.contains("-")){
                for (int i = Integer.parseInt(count.split("-")[0]); i < Integer.parseInt(count.split("-")[1]); i++) {
                    createChessArmorstand(customModelID, p.getLocation().add(i % 8, 0 , i / 8));
                }
                continue;
            }
            if (count.contains(",")){
                for (int i = 0; i < count.split(",").length;i++) {
                    int pos = Integer.parseInt(count.split(",")[i]);
                    createChessArmorstand(customModelID, p.getLocation().add(pos % 8, 0 , pos / 8));
                }
                continue;
            }
            createChessArmorstand(customModelID, p.getLocation().add(Integer.parseInt(count) % 8, 0 , Integer.parseInt(count) / 8));
        }
    }
    
    private static void createChessArmorstand(int customModelID, Location loc){
        ArmorStand armorStand = loc.getWorld().spawn(loc.getBlock().getLocation().add(0.5, -0.1 ,0.5), ArmorStand.class);
        ItemStack item = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta m = item.getItemMeta();
        m.setCustomModelData(customModelID);
        item.setItemMeta(m);

        armorStand.setHelmet(item);
        armorStand.setMarker(true);
        armorStand.setInvisible(true);
    }
}
