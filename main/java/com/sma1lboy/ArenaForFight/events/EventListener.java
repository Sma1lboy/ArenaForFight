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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EventListener implements Listener {
    //store second player
    private Player secondPlayer = null;
    private HashMap<Player, Double> HPManager = new HashMap<>();

    //It called when player interact entity
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player firstPlayer =  event.getPlayer(); //the caller
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
                    this.secondPlayer.closeInventory();
                    this.secondPlayer.sendMessage("TEST");
                    break;
                //disagree fight
                case WOODEN_SWORD:
                    this.secondPlayer.closeInventory();
                    break;
            }
            event.setCancelled(true);
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
