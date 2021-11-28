package com.sma1lboy.ArenaForFight.events;

import com.sma1lboy.ArenaForFight.Commands.GUICommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class ClickGUIEvent implements Listener {

    @EventHandler
    public void clickGUIEvent(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.AQUA+ "GUI")) {
            switch(event.getCurrentItem().getType()) {
                case DIAMOND_SWORD:
                    player.closeInventory();
                    player.sendMessage("nice");

                    break;
                case BARRIER:
                    player.closeInventory();
                    player.sendMessage("close");
                    break;
            }
            event.setCancelled(true);
        }
        if (event.getView().getTitle().equalsIgnoreCase("UI")) {
            switch(event.getCurrentItem().getType()) {
                case DIAMOND_SWORD:
                    player.sendMessage("UI TEST");
                    break;

            }
            event.setCancelled(true);
        }
    }
}
