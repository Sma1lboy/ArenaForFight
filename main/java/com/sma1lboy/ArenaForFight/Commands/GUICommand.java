package com.sma1lboy.ArenaForFight.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUICommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {

        if (sender instanceof Player player) {
            Inventory gui = Bukkit.createInventory(player, 9, ChatColor.AQUA+ "GUI");

            ItemStack fight = new ItemStack(Material.DIAMOND_SWORD);
            ItemStack quit = new ItemStack(Material.BARRIER);

            //Meta for fight
            ItemMeta fightMeta = fight.getItemMeta();
            fightMeta.setDisplayName(ChatColor.RED + "Fight");
            List<String> fightLore = new ArrayList<>();
            fightLore.add(ChatColor.GOLD + "Fight with this guy!");
            fightMeta.setLore(fightLore);
            //set on fight button
            fight.setItemMeta(fightMeta);


            ItemStack[] menu = {fight, quit};

            gui.setContents(menu);
            player.openInventory(gui);

        }
        return true;
    }

}
