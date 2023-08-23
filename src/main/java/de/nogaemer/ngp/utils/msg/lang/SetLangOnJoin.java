package de.nogaemer.ngp.utils.msg.lang;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class SetLangOnJoin implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Lang.getPlayers() == null || !Lang.getPlayers().containsKey(event.getPlayer().getUniqueId().toString())){
            Lang.changeLang(event.getPlayer(), "en_en");
        }
    }

}
