package com.sma1lboy.ArenaForFight.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EventListener implements Listener {
    //store second player
    private Player secondPlayer = null;

    //It called when player interact entity
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player firstPlayer =  event.getPlayer(); //the caller
        Entity entity =  event.getRightClicked();  //the entity interact by player(caller)


        //Create Fight agreement GUI
        Inventory gui = Bukkit.createInventory(firstPlayer, 9, "Arena For Fight");
        //create button
        ItemStack fight = new ItemStack(Material.DIAMOND_SWORD);

        //Set button's lore and meta
        ItemMeta fightMeta = fight.getItemMeta();
        fightMeta.setDisplayName(ChatColor.RED + "Start Fight");
        fightMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
        //Create a lore array
        List<String> fightMetaLore = new ArrayList<>();
        fightMetaLore.add(ChatColor.GOLD + "Click to Start fight with your friend!");
        fightMetaLore.add("XD");
        //Assign back to button from class ItemMeta
        fightMeta.setLore(fightMetaLore);
        fight.setItemMeta(fightMeta);

        gui.setItem(2, fight);
//        FIXME: showing the entity not player right now, test with pig
        if ((entity instanceof Player)) {
            Player secondPlayer = (Player) entity;
            event.setCancelled(true);
            firstPlayer.openInventory(gui);
            //store to class variable
            this.secondPlayer = secondPlayer;
        }

    }

    //it called when someone open the inventory/GUI
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
        if (event.getView().getTitle().equalsIgnoreCase("Arena For Fight")) {
            switch(event.getCurrentItem().getType()) {
                case DIAMOND_SWORD:
                    this.secondPlayer.sendMessage(ChatColor.GOLD + player.getName() + " asks to fight!");
                    break;
            }
            event.setCancelled(true);
        }
    }

}
