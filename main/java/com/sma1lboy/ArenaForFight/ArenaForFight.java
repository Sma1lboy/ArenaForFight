package com.sma1lboy.ArenaForFight;

import com.sma1lboy.ArenaForFight.Commands.GUICommand;
import com.sma1lboy.ArenaForFight.events.EventListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.Objects;

public class ArenaForFight extends JavaPlugin  {

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[ArenaForFight]: ArenaForFight is enable!");
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        Objects.requireNonNull(getServer().getPluginCommand("gui")).setExecutor(new GUICommand());
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ArenaForFight]: ArenaForFight is disabled!");

    }

}

