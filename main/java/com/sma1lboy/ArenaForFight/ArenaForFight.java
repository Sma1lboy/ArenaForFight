package com.sma1lboy.ArenaForFight;
import com.sma1lboy.ArenaForFight.Commands.CheckHealthCommand;
import com.sma1lboy.ArenaForFight.Commands.GUICommand;
import com.sma1lboy.ArenaForFight.events.EventListener;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;
import java.util.*;

public class ArenaForFight extends JavaPlugin implements Listener {

    public MySQL SQL;
    public SQLGetter data;
    @Override
    public void onEnable() {
        this.SQL = new MySQL();
        data = new SQLGetter(this);

        try {
            this.SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            //e.printStackTrace();
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Database not connected!");
        }

        if(this.SQL.isConnected())  {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Database is connected!");
            data.createTable();
        }

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[ArenaForFight]: ArenaForFight is enable!");
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getServer().getPluginCommand("gui")).setExecutor(new GUICommand());
        getServer().getPluginCommand("healthCheck").setExecutor(new CheckHealthCommand());
    }

    @Override
    public void onDisable() {

        SQL.disconnect();
        if(this.SQL.isConnected())  {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Database is disconnected!");
        }
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ArenaForFight]: ArenaForFight is disabled!");

    }


    //event listener part

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        data.createPlayer(player);
    }

    //store second player
    private Player firstPlayer = null;
    private Player secondPlayer = null;
    //private HashMap<Player, Double> HPManager = new HashMap<>();
    //manage player fight match
    public HashMap<Player, Player> playerManager = new HashMap<>();
    //    private HashMap<Player, WorldBorder> worldBorderManager = new HashMap<>();
    //manager player's Inventory
    public HashMap<Player, ItemStack[]> playerInvManager = new HashMap<>();
    //manage players' Health
    public HashMap<Player, Double> playerHealthManager = new HashMap<>();





    //GUI PART
    //It called when player interact entity
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        this.firstPlayer =  event.getPlayer(); //the caller
        //creat the gui
        Inventory gui = setMainMenu();

//        FIXME: showing the entity not player right now, test with pig
        if ((event.getRightClicked() instanceof Player secondPlayer) && firstPlayer.isSneaking()) {
            //store to class variable
            this.secondPlayer = secondPlayer;
            event.setCancelled(true);
            //Prevent the player can reopen the GUI when they are fighting
            if(!(playerManager.containsKey(this.firstPlayer) || playerManager.containsValue(this.firstPlayer))) {
                firstPlayer.openInventory(gui);
            }
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
        Inventory acceptGui = null;
        //it called when someone right-clicked another player to ask to fight
        if (event.getView().getTitle().equalsIgnoreCase("Arena For Fight")) {
            switch(Objects.requireNonNull(event.getCurrentItem()).getType()) {
                case DIAMOND_SWORD:
                    acceptGui = setAcceptMenu("SOLO");
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    player.closeInventory();
                    this.secondPlayer.sendMessage(ChatColor.GOLD + player.getName() + " asks to fight!");
                    this.secondPlayer.openInventory(acceptGui);
                    break;
                case GOLDEN_SWORD:
                    acceptGui = setAcceptMenu("Random Items Fight");
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    player.closeInventory();
                    this.secondPlayer.sendMessage(ChatColor.GOLD + player.getName() + " asks to fight!");
                    this.secondPlayer.openInventory(acceptGui);
                    break;
                case BARRIER:
                    player.closeInventory();
            }
            event.setCancelled(true);
        }

        //it called when someone after right-clicked, another player will get a gui to agree or disagree
        if (event.getView().getTitle().equalsIgnoreCase("SOLO")) {
            switch(Objects.requireNonNull(event.getCurrentItem()).getType()) {
                //agree fight
                case DIAMOND_SWORD:
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    this.secondPlayer.closeInventory();
                    firstPlayer.sendTitle(ChatColor.GREEN + "Fight Start!", "Go punch his face!", 1, 40, 1);
                    secondPlayer.sendTitle(ChatColor.GREEN + "Fight Start!", "Go punch his face!", 1, 40, 1);
                    playerHealthManager.put(firstPlayer, firstPlayer.getHealth());
                    playerHealthManager.put(secondPlayer,secondPlayer.getHealth());
                    //FIXME: needs to change to maximum health
                    firstPlayer.setHealth(20);
                    secondPlayer.setHealth(20);

                    playerManager.put(this.firstPlayer, this.secondPlayer);

                    //FIXME: delete next line after finish TEST
                    firstPlayer.sendMessage(playerManager.toString());

                    break;
                //disagree fight
                case WOODEN_SWORD:
                    secondPlayer.closeInventory();
                    break;
            }
            event.setCancelled(true);
        }
        //random Items fight
        if(event.getView().getTitle().equalsIgnoreCase("Random Items Fight")) {
            switch(Objects.requireNonNull(event.getCurrentItem()).getType()) {
                //agree
                case DIAMOND_SWORD:
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    this.secondPlayer.closeInventory();
                    firstPlayer.sendTitle(ChatColor.GREEN + "Fight Start!", "Go punch his face!", 1, 40, 1);
                    secondPlayer.sendTitle(ChatColor.GREEN + "Fight Start!", "Go punch his face!", 1, 40, 1);
                    playerHealthManager.put(firstPlayer, firstPlayer.getHealth());
                    playerHealthManager.put(secondPlayer,secondPlayer.getHealth());
                    //FIXME: needs to change to maximum health
                    firstPlayer.setHealth(20);
                    secondPlayer.setHealth(20);
                    //FIXME: health check
                    firstPlayer.sendMessage(playerHealthManager.toString());
                    playerInvManager.put(firstPlayer, firstPlayer.getInventory().getContents());
                    playerInvManager.put(secondPlayer, secondPlayer.getInventory().getContents());
                    playerManager.put(this.firstPlayer, this.secondPlayer);
                    firstPlayer.getInventory().clear();
                    secondPlayer.getInventory().clear();

                    //makes the weapon more fun or something that!
                    ItemStack[] mainHandWeapon = {new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.WOODEN_SWORD),
                            new ItemStack(Material.IRON_AXE)};
                    ItemStack[] chestPlate = {new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.IRON_CHESTPLATE),
                            new ItemStack(Material.LEATHER_CHESTPLATE)};

                    firstPlayer.getInventory().setItemInMainHand(mainHandWeapon[randomPick(mainHandWeapon.length)]);
                    firstPlayer.getInventory().setChestplate(chestPlate[randomPick(chestPlate.length)]);
                    secondPlayer.getInventory().setItemInMainHand(mainHandWeapon[randomPick(mainHandWeapon.length)]);
                    secondPlayer.getInventory().setChestplate(chestPlate[randomPick(chestPlate.length)]);


                    break;
                //disagree
                case WOODEN_SWORD:
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                    this.secondPlayer.closeInventory();
                    break;

            }
        }
    }
    private int randomPick(int i) {
        Random rnd = new Random();
        return rnd.nextInt(i);
    }


    // FIGHT PART
    //    it called when entity get damage from another entity
    @EventHandler
    public void onPlayerFightingPlayer(EntityDamageByEntityEvent e) {

        if (playerManager.containsValue((Player)e.getDamager()) || playerManager.containsKey((Player)e.getDamager())) {
            //getEntity giving who is getting damage
            e.getEntity().getName();

            Player playerGotDmg = (Player) e.getEntity();

            if ((playerGotDmg.getHealth() - e.getDamage()) < 0.5){
                e.setCancelled(true);
                playerGotDmg.sendTitle(ChatColor.RED + "You lose!", "Don't worry, win back next time!", 1, 100, 1);
                ((Player) e.getDamager()).sendTitle(ChatColor.GOLD + "You WIN!", "Keep Going!", 1, 100, 1);

                Player winner = (Player) e.getDamager();
                data.addPoints(winner.getUniqueId(), 1);
                e.getDamager().sendMessage(winner.getUniqueId().toString());
                e.getDamager().sendMessage("add");

                ((Player) e.getDamager()).setHealth(playerHealthManager.get((Player)e.getDamager()));
                ((Player) e.getEntity()).setHealth(playerHealthManager.get((Player) e.getEntity()));
                //if random item mode, it will give back player's inventory back
                if(playerInvManager.containsKey((Player) e.getDamager())) {
                    ((Player) e.getDamager()).getInventory().setContents(playerInvManager.get((Player) e.getDamager()));
                    ((Player) e.getEntity()).getInventory().setContents(playerInvManager.get((Player) e.getEntity()));

                }
                //remove both player from the hashmap to prevent keep they can't hit anything
                if (playerManager.containsKey((Player)e.getDamager())) {
                    playerManager.remove((Player) e.getDamager());
                }
                else {
                    playerManager.remove((Player)e.getEntity());
                }
                playerHealthManager.remove((Player) e.getEntity());
                playerHealthManager.remove((Player) e.getDamager());
                playerInvManager.remove((Player)e.getEntity());
                playerInvManager.remove((Player)e.getDamager());
                //FIXME: check clear or not
                firstPlayer.sendMessage(playerManager.toString());

                //Firework to the winner!!!!!!!!!!!!!!
                Firework winnerFirework = e.getDamager().getWorld().spawn(e.getDamager().getLocation(), Firework.class);
                FireworkMeta data = winnerFirework.getFireworkMeta();
                data.addEffect(FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
                data.setPower(0);
                winnerFirework.setFireworkMeta(data);
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
    public void playerBreakBlockEvent(BlockBreakEvent e) {
        if(playerManager.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    //prevent player death by falling or mob kill when they are fighting
    @EventHandler
    public void playerDeathEvent(EntityDeathEvent e) {
        if(playerManager.containsKey((Player)e.getEntity())) {
            playerManager.remove((Player)e.getEntity());
        }
    }

    private Inventory setMainMenu() {
        //Create Fight agreement GUI
        Inventory gui = Bukkit.createInventory(null, 9, "Arena For Fight");
        //create button
        ItemStack fight = new ItemStack(Material.DIAMOND_SWORD);
        //create random items fight
        ItemStack rndItemFight = new ItemStack(Material.GOLDEN_SWORD);
        ItemStack quit = new ItemStack(Material.BARRIER);

        //Set FIGHT  button's lore and meta
        ItemMeta fightMeta = fight.getItemMeta();
        assert fightMeta != null;
        fightMeta.setDisplayName(ChatColor.WHITE + "Start SOLO Fight");
        fightMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
        fightMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        //Create a lore
        List<String> fightMetaLore = new ArrayList<>();
        fightMetaLore.add(ChatColor.GOLD + "Click to Start fight with your friend!");
        fightMetaLore.add("XD");
        //Assign back to button from class ItemMeta
        fightMeta.setLore(fightMetaLore);
        fight.setItemMeta(fightMeta);

        ItemMeta rndItemFightMeta = rndItemFight.getItemMeta();
        assert rndItemFightMeta != null;
        rndItemFightMeta.setDisplayName(ChatColor.WHITE + "Start random items Fight");
        rndItemFightMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
        rndItemFightMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        //Create a lore
        List<String> rndItemFightMetaLore = new ArrayList<>();
        rndItemFightMetaLore.add(ChatColor.GOLD + "Diamond or wood?");
        //Assign back to button from class ItemMeta
        rndItemFightMeta.setLore(rndItemFightMetaLore);
        rndItemFight.setItemMeta(rndItemFightMeta);


        //SET quit button
        ItemMeta quitMeta = quit.getItemMeta();
        quitMeta.setDisplayName(ChatColor.RED + "Quit");
        quit.setItemMeta(quitMeta);

        gui.setItem(2, fight);
        gui.setItem(4, rndItemFight);
        gui.setItem(8, quit);
        return gui;

    }
    private Inventory setAcceptMenu(String GuiName) {
//        create accept GUi after someone called ask fight
        Inventory acceptGui = Bukkit.createInventory(null, 9, GuiName);
        ItemStack agree = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack disagree = new ItemStack(Material.WOODEN_SWORD);

        //add meta and lore to Agree button
        ItemMeta agreeMeta = agree.getItemMeta();
        assert agreeMeta != null;
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

