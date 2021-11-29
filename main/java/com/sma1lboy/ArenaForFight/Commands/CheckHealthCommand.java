package com.sma1lboy.ArenaForFight.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckHealthCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {

        if (sender instanceof Player player) {
            player.sendMessage(String.valueOf(player.getHealth()));
        }
        return true;
    }
}
