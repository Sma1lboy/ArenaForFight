package com.sma1lboy.ArenaForFight.events;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Handler;

public class EventListener implements Listener {
    //store second player
    private Player firstPlayer = null;
    private Player secondPlayer = null;
    //private HashMap<Player, Double> HPManager = new HashMap<>();
    public HashMap<Player, Player> playerManager = new HashMap<>();
//    private HashMap<Player, WorldBorder> worldBorderManager = new HashMap<>();


    //It called when player interact entity
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        this.firstPlayer =  event.getPlayer(); //the caller
        Entity entity =  event.getRightClicked();  //the entity interact by player(caller)

        //creat the gui
        Inventory gui = setMainMenu();

//        FIXME: showing the entity not player right now, test with pig
        if ((entity instanceof Player secondPlayer)) {
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
            switch(Objects.requireNonNull(event.getCurrentItem()).getType()) {
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

        //create a gui for accept fight or not
        Inventory acceptGui = setAcceptMenu();

        //it called when someone right-clicked another player to ask to fight
        if (event.getView().getTitle().equalsIgnoreCase("Arena For Fight")) {
            switch(Objects.requireNonNull(event.getCurrentItem()).getType()) {
                case DIAMOND_SWORD:
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    player.closeInventory();
                    this.secondPlayer.sendMessage(ChatColor.GOLD + player.getName() + " asks to fight!");
                    this.secondPlayer.openInventory(acceptGui);
                    break;
            }
            event.setCancelled(true);
        }
        //it called when someone after right-clicked, another player will get a gui to agree or disagree
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED +  "Arena For Fight responder")) {
            switch(Objects.requireNonNull(event.getCurrentItem()).getType()) {
                //agree fight
                case DIAMOND_SWORD:
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    this.secondPlayer.closeInventory();
                    //FIXME: needs to change to maximum health
//                    this.firstPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
//                    this.secondPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    firstPlayer.setHealth(20);
                    secondPlayer.setHealth(20);
                    firstPlayer.sendTitle(ChatColor.GREEN + "Fight Start!", "Go punch his face!", 1, 40, 1);
                    secondPlayer.sendTitle(ChatColor.GREEN + "Fight Start!", "Go punch his face!", 1, 40, 1);

                    playerManager.put(this.firstPlayer, this.firstPlayer);
                    playerManager.put(this.secondPlayer, this.secondPlayer);
                    //FIXME: delete next line after finish TEST
                    firstPlayer.sendMessage(playerManager.toString());


                    break;
                //disagree fight
                case WOODEN_SWORD:
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                    this.secondPlayer.closeInventory();
                    break;
            }
            event.setCancelled(true);
        }
    }

    //    it called when entity get damage from another entity
    // FIXME: change the event to update player health frequency
    @EventHandler
    public void onPlayerFightingPlayer(EntityDamageByEntityEvent e) {
        //FIXME: TEST
        e.getEntity().sendMessage(String.valueOf(e.getDamager().getType()));
        if (playerManager.containsValue((Player)e.getDamager())) {

            //getEntity giving who is getting damage
            e.getEntity().getName();
            Player playerGotDmg = playerManager.get((Player) e.getEntity());
            //FIXME: test hp, delet next 2 lines
            playerGotDmg.sendMessage(String.valueOf(playerGotDmg.getHealth()));
            playerGotDmg.sendMessage("Damage was "+ String.valueOf(e.getDamage()));
            //FIXME: change the hp value
           if ((playerGotDmg.getHealth() - e.getDamage()) < 0.5){
               e.setCancelled(true);
               playerGotDmg.sendTitle(ChatColor.RED + "You lose!", "Don't worry, win back next time!", 1, 100, 1);
               ((Player) e.getDamager()).sendTitle(ChatColor.GOLD + "You WIN!", "Keep Going!", 1, 100, 1);
                //Firework to the winner!!!!!!!!!!!!!!
               Firework winnerFirework = e.getDamager().getWorld().spawn(e.getDamager().getLocation(), Firework.class);
               FireworkMeta data = winnerFirework.getFireworkMeta();
               data.addEffect(FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
               data.setPower(0);
               winnerFirework.setFireworkMeta(data);

               playerManager.get((Player)e.getDamager()).setHealth(20);
               playerManager.get((Player)e.getEntity()).setHealth(20);
               playerManager.remove((Player)e.getDamager());
               playerManager.remove((Player)e.getEntity());
               //FIXME: check clear or not
               firstPlayer.sendMessage(playerManager.toString());
               //还没有增加获胜计数系统 或者什么奖励系统

           }
        }
    }
    // it called prevent player pick up item when they are fighting
    @EventHandler
    public void playerPickupItemEvent(PlayerPickupItemEvent e) {
        if (playerManager.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    // it called prevent player drops item when they are fighting
    @EventHandler
    public void playerDropItemEvent(PlayerDropItemEvent e ) {
        if(playerManager.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    // prevent player place block
    @EventHandler
    public void playerPlaceBlockEvent(BlockPlaceEvent e) {
        if(playerManager.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    // prevent player break the block
    @EventHandler
    public void playerPlaceBlockEvent(BlockBreakEvent e) {
        if(playerManager.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

//    UPDATE: delete setGui method because its not useful
//    private Inventory setGui(String guiType) {
//        switch (guiType) {
//            case "setMainMenu":
//                setMainMenu();
//                break;
//            case "setAcceptMenu":
//                setAcceptMenu();
//                break;
//
//        }
//        return null;
//    }
    private Inventory setMainMenu() {
        //Create Fight agreement GUI
         Inventory gui = Bukkit.createInventory(null, 9, "Arena For Fight");
        //create button
        ItemStack fight = new ItemStack(Material.DIAMOND_SWORD);

        //Set button's lore and meta
        ItemMeta fightMeta = fight.getItemMeta();
        fightMeta.setDisplayName(ChatColor.RED + "Start Fight");
        fightMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
        fightMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        //Create a lore array
        List<String> fightMetaLore = new ArrayList<>();
        fightMetaLore.add(ChatColor.GOLD + "Click to Start fight with your friend!");
        fightMetaLore.add("XD");
        //Assign back to button from class ItemMeta
        fightMeta.setLore(fightMetaLore);
        fight.setItemMeta(fightMeta);
        gui.setItem(2, fight);
        return gui;

    }
    private Inventory setAcceptMenu() {
//        create accept GUi after someone called ask fight
        Inventory acceptGui = Bukkit.createInventory(null, 9, ChatColor.RED +  "Arena For Fight responder");
        ItemStack agree = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack disagree = new ItemStack(Material.WOODEN_SWORD);

        //add meta and lore to Agree button
        ItemMeta agreeMeta = agree.getItemMeta();
        agreeMeta.setDisplayName(ChatColor.GREEN + "Agree fight");
        agreeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1 , false);
        agreeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<String> agreeMetaLore = new ArrayList<>();
        agreeMetaLore.add(ChatColor.GOLD + "Agree to fight with you friend!");
        agreeMeta.setLore(agreeMetaLore);
        agree.setItemMeta(agreeMeta);
        //add meta and lore to Disagree button
        ItemMeta disagreeMeta = agree.getItemMeta();
        disagreeMeta.setDisplayName(ChatColor.RED + "Disagree fight");
        disagreeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1 , false);
        disagreeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<String> disagreeMetaLore = new ArrayList<>();
        disagreeMetaLore.add(ChatColor.GOLD + "Quit");
        disagreeMeta.setLore(disagreeMetaLore);
        disagree.setItemMeta(disagreeMeta);


        acceptGui.setItem(2, agree);
        acceptGui.setItem(6, disagree);

        return  acceptGui;
    }

}
