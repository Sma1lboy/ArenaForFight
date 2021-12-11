package com.sma1lboy.ArenaForFight.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckHealthCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {

        if (sender instanceof Player player) {
            if (player.hasPermission("arenaforfight.healthcheck")) {
                player.sendMessage("Health is: " + String.valueOf(player.getHealth()));
            }
            else {
                player.sendMessage(ChatColor.RED + "You do not have the required permission(arenaforfight.healthcheck) to run this command!");
            }
        }
        return true;
    }
}
