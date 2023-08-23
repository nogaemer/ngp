package de.nogaemer.ngp.utils.menues.gui.challenges;

import de.nogaemer.ngp.challenges.ForceBattle;
import de.nogaemer.ngp.challenges.HBR;
import de.nogaemer.ngp.challenges.Randomizer;
import de.nogaemer.ngp.utils.menues.gui.Gui;
import de.nogaemer.ngp.utils.menues.gui.OverviewGUI;
import de.nogaemer.ngp.utils.menues.gui.command.GuiCommand;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChallengeGUIRegistry implements Listener {
    public static void setup(){
        ChallengeGUI.addItem(Material.CHAINMAIL_BOOTS, "forcebattle", "menus.challenge.overview.items.force_battle", ForceBattle.getInvs());
        ChallengeGUI.addItem(Material.BEDROCK, "hbr", "menus.challenge.overview.items.hbr", HBR.getInvs());
        ChallengeGUI.addItem(Material.GRAY_GLAZED_TERRACOTTA, "randomizer", "menus.challenge.overview.items.randomizer", Randomizer.getInvs());
        ChallengeGUI.addItem(Material.CHEST_MINECART, "4", "items.null");
        ChallengeGUI.addItem(Material.BARREL, "5", "items.null");
        ChallengeGUI.addItem(Material.DIAMOND_PICKAXE, "6", "items.null");
        ChallengeGUI.addItem(Material.GRASS, "7", "items.null");
        ChallengeGUI.addItem(Material.MAGENTA_BANNER, "8", "items.null");
        ChallengeGUI.addItem(Material.RED_CANDLE, "9", "items.null");


        ChallengeGUI.setup();
    }

    private static void register(ItemStack item){
        switch (item.getItemMeta().getLocalizedName()){
            case "forcebattle" :
                ForceBattle.setRunning(!ForceBattle.running);
                break;
            case "hbr" :
                HBR.setRunning(!HBR.running);
                break;
            case "randomizer" :
                Randomizer.setRunning(!Randomizer.running);
                break;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null){
            ItemStack item = event.getCurrentItem();

            for (Integer integer : ChallengeGUI.getPages().keySet()) {
                if (ChallengeGUI.getPages().get(integer).containsValue(event.getClickedInventory())){
                    event.setCancelled(true);
                    Player p = (Player) event.getWhoClicked();
                    switch (item.getItemMeta().getLocalizedName()){
                        case "right" :
                            if (integer < ChallengeGUI.getPages().size()-1){
                                Gui.switchInventory(event.getClickedInventory(), ChallengeGUI.getPages().get(integer + 1).get(Lang.getLangName(p)), p, "right");
                            }
                            break;
                        case "left" :
                            if (integer > 0){
                                Gui.switchInventory(event.getClickedInventory(), ChallengeGUI.getPages().get(integer - 1).get(Lang.getLangName(p)), p, "left");
                            } else if (integer == 0){
                                Gui.switchInventory(event.getClickedInventory(), (Inventory) OverviewGUI.getInvs().get(Lang.getLangName(p)), p, "left");
                            }
                            break;
                    }
                    if (ChallengeGUI.nameInv.containsKey(item.getItemMeta().getLocalizedName()) && item.getType() != Material.LIME_CONCRETE && item.getType() != Material.RED_CONCRETE ){
                        Gui.switchInventory(event.getClickedInventory(), ChallengeGUI.nameInv.get(item.getItemMeta().getLocalizedName()).get(Lang.getLangName(p)), p, "up");
                    } else if (ChallengeGUI.loc.containsValue(item.getItemMeta().getLocalizedName()) &&!ChallengeGUI.nameInv.containsKey(item) && item.getType() != Material.LIME_CONCRETE && item.getType() != Material.RED_CONCRETE){
                        register(item);
                        if (event.getClickedInventory().getItem(event.getSlot() + 9).getType() == Material.LIME_CONCRETE){
                            ChallengeGUI.getPages().get(integer).forEach((s, inventory) -> inventory.setItem(event.getSlot() + 9, ChallengeGUI.red.get(s)));
                        } else if (event.getClickedInventory().getItem(event.getSlot() + 9).getType() == Material.RED_CONCRETE){
                            ChallengeGUI.getPages().get(integer).forEach((s, inventory) -> inventory.setItem(event.getSlot() + 9, ChallengeGUI.green.get(s)));
                        }
                    } else if (item.getType() == Material.LIME_CONCRETE || item.getType() == Material.RED_CONCRETE){
                        register(event.getClickedInventory().getItem(event.getSlot()-9));
                        /*if (event.getClickedInventory().getItem(event.getSlot()).getType() == Material.LIME_CONCRETE){
                            ChallengeGUI.getPages().get(integer).forEach((s, inventory) -> inventory.setItem(event.getSlot(), ChallengeGUI.red.get(s)));
                        } else if (event.getClickedInventory().getItem(event.getSlot()).getType() == Material.RED_CONCRETE){
                            ChallengeGUI.getPages().get(integer).forEach((s, inventory) -> inventory.setItem(event.getSlot(), ChallengeGUI.green.get(s)));
                        }*/
                    }
                }
            }
        }
    }
}
