package de.nogaemer.ngp.utils.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {

    public static boolean isSimilar(ItemStack firstItem,  ItemStack secondItem){
        ItemMeta firstM = firstItem.getItemMeta();
        ItemMeta secondM = secondItem.getItemMeta();

        assert secondM != null;
        assert firstM != null;
        assert secondM.getLore() != null;
        assert firstM.getLore() != null;
        assert secondM != null;
        assert firstM != null;

        if
        (
                firstM.getDisplayName().equals(secondM.getDisplayName()) &&
                firstM.getLore().equals(secondM.getLore()) &&
                firstItem.getDurability() == secondItem.getDurability() &&
                firstItem.getType().equals(secondItem.getType())
        ){
            return true;
        } else {
            return false;
        }
    }
}
