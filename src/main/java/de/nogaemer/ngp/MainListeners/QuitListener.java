package de.nogaemer.ngp.MainListeners;

import de.nogaemer.ngp.Main;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class QuitListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        if (Main.getInstance().getConfig().getBoolean("isReset"))
            event.setReason((String) Lang.msg("message.world-reset", event.getPlayer()));
    }
}
