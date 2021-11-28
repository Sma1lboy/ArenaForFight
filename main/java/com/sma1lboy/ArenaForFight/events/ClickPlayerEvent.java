package com.sma1lboy.ArenaForFight.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickPlayerEvent implements Listener {
    @EventHandler
    public void clickPlayerEvent(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        if(e.getRightClicked() != player) {
        }
    }
}
