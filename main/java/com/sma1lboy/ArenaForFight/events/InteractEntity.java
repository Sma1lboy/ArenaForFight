package com.sma1lboy.ArenaForFight.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InteractEntity implements Listener {
    @EventHandler
    public static void onPlayerInteractEntity (PlayerInteractEntityEvent event) {
        Player firstPlayer =  event.getPlayer(); //the caller
        Entity entity =  event.getRightClicked();  //the entity interact by player(caller)

        Inventory gui = Bukkit.createInventory(firstPlayer, 9, "UI");
        ItemStack fight = new ItemStack(Material.DIAMOND_SWORD);
        gui.setItem(0, fight);

//        FIXME: showing the entity not player right now, test with pig
        if (!(entity instanceof Player)) {
            Player secondPlayer = (Player) entity;
            event.setCancelled(true);
            firstPlayer.openInventory(gui);
            secondPlayer.openInventory(gui);
        }
    }
}

//    //            The first player location
//    int firstPlayerX = firstPlayer.getLocation().getBlockX();
//    int firstPlayerY = firstPlayer.getLocation().getBlockY();
//    int firstPlayerZ = firstPlayer.getLocation().getBlockZ();
//    //            The second player location
//    int secondPlayerX = secondPlayer.getLocation().getBlockX();
//    int secondPlayerY = secondPlayer.getLocation().getBlockY();
//    int secondPlayerZ = secondPlayer.getLocation().getBlockZ();
//
////            DEBUG: Test the location from player and entity
////            event.getPlayer().sendMessage(firstPlayerX + " " + firstPlayerY + " " + firstPlayerZ);
////            event.getPlayer().sendMessage(secondPlayerX + " " + secondPlayerY + " " + secondPlayerZ);
//
////            equation for the substance square
//                for (int i = 0; i < 10; ++i) {
//        for (int j = 0; j < 10; ++j) {
//        for (int k = 0; k < 10; ++k) {
//        firstPlayer.getWorld().getBlockAt(((firstPlayerX + secondPlayerX) / 2) + i,
//        ((firstPlayerY + secondPlayerY) / 2) + j, ((firstPlayerZ + secondPlayerZ) / 2) + k).setType(Material.BARRIER);
//        }
//        }
//        }
////            subtract the substance from the square
//        //FIXME: because it is not gonna edit the limit size in game, not planning to yet
//        for (int i = 0; i < 8; ++i) {
//        for (int j = 0; j < 20; ++j) {
//        for (int k = 0; k < 8; ++k) {
//        firstPlayer.getWorld().getBlockAt(((firstPlayerX + secondPlayerX) / 2) + i + 1,
//        ((firstPlayerY + secondPlayerY) / 2) + j, ((firstPlayerZ + secondPlayerZ) / 2) + k + 1).setType(Material.AIR);
//        }
//        }
//        }