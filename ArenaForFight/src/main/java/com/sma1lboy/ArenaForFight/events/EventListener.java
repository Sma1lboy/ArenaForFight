package com.sma1lboy.ArenaForFight.events;
import com.sma1lboy.ArenaForFight.ArenaForFight;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class EventListener implements Listener  {
    private final ArenaForFight plugin;
    public EventListener(ArenaForFight plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        this.plugin.sqlGetter.createPlayer(player);
    }

    //store second player
    private Player firstPlayer = null;
    private Player secondPlayer = null;
    //manage player fight match
    public HashMap<Player, Player> playerManager = new HashMap<>();
    //manager player's Inventory
    public HashMap<Player, ItemStack[]> playerInvManager = new HashMap<>();
    //manage players' Health
    public HashMap<Player, Double> playerHealthManager = new HashMap<>();

    //GUI PART
    //It called when player interact entity
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        this.firstPlayer =  event.getPlayer(); //the caller
        //create the gui
        Inventory gui = setMainMenu();
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
//    //it called when someone open the inventory/GUI
    @EventHandler
    public void clickGUIEvent(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.AQUA+ "GUI")) {
            switch (Objects.requireNonNull(event.getCurrentItem()).getType()) {
                case DIAMOND_SWORD -> {
                    player.closeInventory();
                    player.sendMessage("nice");
                }
                case BARRIER -> {
                    player.closeInventory();
                    player.sendMessage("close");
                }
            }
            event.setCancelled(true);
        }

        //create a gui for accept fight or not
        Inventory acceptGui;
        //it called when someone right-clicked another player to ask to fight
        if ("Arena For Fight".equalsIgnoreCase(event.getView().getTitle())) {
            switch (Objects.requireNonNull(event.getCurrentItem()).getType()) {
                case DIAMOND_SWORD -> {
                    acceptGui = setAcceptMenu("SOLO");
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    player.closeInventory();
                    this.secondPlayer.sendMessage(ChatColor.GOLD + player.getName() + getConfigText("askFight"));
                    this.secondPlayer.openInventory(acceptGui);
                }
                case GOLDEN_SWORD -> {
                    acceptGui = setAcceptMenu("Random Items Fight");
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    player.closeInventory();
                    this.secondPlayer.sendMessage(ChatColor.GOLD + player.getName() + getConfigText("askFight"));
                    this.secondPlayer.openInventory(acceptGui);
                }
                case BARRIER -> player.closeInventory();
            }
            event.setCancelled(true);
        }

        //it called when someone after right-clicked, another player will get a gui to agree or disagree
        if ("SOLO".equalsIgnoreCase(event.getView().getTitle())) {
            switch (Objects.requireNonNull(event.getCurrentItem()).getType()) {
                //agree fight
                case DIAMOND_SWORD -> {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    this.secondPlayer.closeInventory();
                    firstPlayer.sendTitle(ChatColor.GREEN + getConfigText("fightStartTitle"), getConfigText("fightStartSubtitle"), 1, 40, 1);
                    secondPlayer.sendTitle(ChatColor.GREEN + getConfigText("fightStartTitle"), getConfigText("fightStartSubtitle"), 1, 40, 1);
                    playerHealthManager.put(firstPlayer, firstPlayer.getHealth());
                    playerHealthManager.put(secondPlayer, secondPlayer.getHealth());
                    //FIXME: needs to change to maximum health
                    firstPlayer.setHealth(20);
                    secondPlayer.setHealth(20);
                    playerManager.put(this.firstPlayer, this.secondPlayer);

                    //FIXME: delete next line after finish TEST
                    firstPlayer.sendMessage(playerManager.toString());
                }
                //disagree fight
                case WOODEN_SWORD -> secondPlayer.closeInventory();
            }
            event.setCancelled(true);
        }
        //random Items fight
        if("Random Items Fight".equalsIgnoreCase(event.getView().getTitle())) {
            switch (Objects.requireNonNull(event.getCurrentItem()).getType()) {
                //agree
                case DIAMOND_SWORD -> {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    this.secondPlayer.closeInventory();
                    firstPlayer.sendTitle(ChatColor.GREEN + plugin.getConfig().getString(plugin.language + ".fightStartTitle"),
                            getConfigText("fightStartSubtitle"), 1, 40, 1);
                    secondPlayer.sendTitle(ChatColor.GREEN + getConfigText("fightStartTitle"), getConfigText("fightStartSubtitle"), 1, 40, 1);
                    playerHealthManager.put(firstPlayer, firstPlayer.getHealth());
                    playerHealthManager.put(secondPlayer, secondPlayer.getHealth());
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
                    ItemStack[] helmet = {new ItemStack(Material.DIAMOND_HELMET)};
                    ItemStack[] chestPlate = {new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.IRON_CHESTPLATE),
                            new ItemStack(Material.LEATHER_CHESTPLATE)};
                    ItemStack[] legs = {new ItemStack(Material.LEATHER_LEGGINGS)};
                    ItemStack[] boots = {new ItemStack(Material.IRON_BOOTS)};
                    firstPlayer.getInventory().setItemInMainHand(mainHandWeapon[randomPick(mainHandWeapon.length)]);
                    firstPlayer.getInventory().setHelmet(helmet[randomPick(helmet.length)]);
                    firstPlayer.getInventory().setChestplate(chestPlate[randomPick(chestPlate.length)]);
                    firstPlayer.getInventory().setLeggings(legs[randomPick(legs.length)]);
                    firstPlayer.getInventory().setBoots(boots[randomPick(boots.length)]);
                    secondPlayer.getInventory().setItemInMainHand(mainHandWeapon[randomPick(mainHandWeapon.length)]);
                    secondPlayer.getInventory().setChestplate(chestPlate[randomPick(chestPlate.length)]);
                }
                //disagree
                case WOODEN_SWORD -> {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                    this.secondPlayer.closeInventory();
                }
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

        /*
        You need to check if the Damager and receiver is a player. The damager can also be a skeleton or a zombie
         */
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {

            if (playerManager.containsValue((Player) e.getDamager()) || playerManager.containsKey((Player) e.getDamager())) {
                //getEntity giving who is getting damage
                e.getEntity().getName();

                Player playerGotDmg = (Player) e.getEntity();

                if ((playerGotDmg.getHealth() - e.getDamage()) < 0.5) {
                    e.setCancelled(true);
                    playerGotDmg.sendTitle(ChatColor.RED + getConfigText("lose"), getConfigText("loseSubline"), 1, 100, 1);
                    ((Player) e.getDamager()).sendTitle(ChatColor.GOLD + getConfigText("win"), getConfigText("winSubline"), 1, 100, 1);

                    //TODO counts system with mysql


                    ((Player) e.getDamager()).setHealth(playerHealthManager.get((Player) e.getDamager()));
                    ((Player) e.getEntity()).setHealth(playerHealthManager.get((Player) e.getEntity()));
                    //if random item mode, it will give back player's inventory back
                    if (playerInvManager.containsKey((Player) e.getDamager())) {
                        ((Player) e.getDamager()).getInventory().setContents(playerInvManager.get((Player) e.getDamager()));
                        ((Player) e.getEntity()).getInventory().setContents(playerInvManager.get((Player) e.getEntity()));

                    }
                    //remove both player from the hashmap to prevent keep they can't hit anything
                    if (playerManager.containsKey((Player) e.getDamager())) {
                        playerManager.remove((Player) e.getDamager());
                    } else {
                        playerManager.remove((Player) e.getEntity());
                    }
                    playerHealthManager.remove((Player) e.getEntity());
                    playerHealthManager.remove((Player) e.getDamager());
                    playerInvManager.remove((Player) e.getEntity());
                    playerInvManager.remove((Player) e.getDamager());
                    //FIXME: check clear or not
                    firstPlayer.sendMessage(playerManager.toString());

                    //Firework to the winner!!!!!!!!!!!!!!
                    Firework winnerFirework = e.getDamager().getWorld().spawn(e.getDamager().getLocation(), Firework.class);
                    FireworkMeta fireMeta = winnerFirework.getFireworkMeta();
                    fireMeta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
                    fireMeta.setPower(0);

                    //signd
                    winnerFirework.setFireworkMeta(fireMeta);
                }
            }
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
        fightMeta.setDisplayName(ChatColor.WHITE + plugin.getConfig().getString(plugin.language + ".soloButton"));
        fightMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
        fightMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        //Create a lore
        List<String> fightMetaLore = new ArrayList<>();
        fightMetaLore.add(ChatColor.GOLD + plugin.getConfig().getString(plugin.language + ".soloButtonLore"));
        fightMetaLore.add("XD");
        //Assign back to button from class ItemMeta
        fightMeta.setLore(fightMetaLore);
        fight.setItemMeta(fightMeta);

        ItemMeta rndItemFightMeta = rndItemFight.getItemMeta();
        assert rndItemFightMeta != null;
        rndItemFightMeta.setDisplayName(ChatColor.WHITE + plugin.getConfig().getString(plugin.language + ".randomFightButton"));
        rndItemFightMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
        rndItemFightMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        //Create a lore
        List<String> rndItemFightMetaLore = new ArrayList<>();
        rndItemFightMetaLore.add(ChatColor.GOLD + plugin.getConfig().getString(plugin.language + ".randomFightButtonLore"));
        //Assign back to button from class ItemMeta
        rndItemFightMeta.setLore(rndItemFightMetaLore);
        rndItemFight.setItemMeta(rndItemFightMeta);


        //SET quit button
        ItemMeta quitMeta = quit.getItemMeta();
        assert quitMeta != null;
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
        agreeMeta.setDisplayName(ChatColor.GREEN + getConfigText("agreeFight"));
        agreeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1 , false);
        agreeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<String> agreeMetaLore = new ArrayList<>();
        agreeMetaLore.add(ChatColor.GOLD + getConfigText("agreeFightLore"));
        agreeMeta.setLore(agreeMetaLore);
        agree.setItemMeta(agreeMeta);
        //add meta and lore to Disagree button
        ItemMeta disagreeMeta = agree.getItemMeta();
        disagreeMeta.setDisplayName(ChatColor.RED + getConfigText("disagreeFight"));
        disagreeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1 , false);
        disagreeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        List<String> disagreeMetaLore = new ArrayList<>();
        disagreeMetaLore.add(ChatColor.GOLD + getConfigText("disagreeFightLore"));
        disagreeMeta.setLore(disagreeMetaLore);
        disagree.setItemMeta(disagreeMeta);

        acceptGui.setItem(2, agree);
        acceptGui.setItem(6, disagree);

        return  acceptGui;

    }
    //reduce redundancy
    public String getConfigText(String str){
        return plugin.getConfig().getString(plugin.language + "." + str);
    }
}
