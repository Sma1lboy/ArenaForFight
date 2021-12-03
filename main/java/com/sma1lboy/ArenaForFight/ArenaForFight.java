package com.sma1lboy.ArenaForFight;
import com.sma1lboy.ArenaForFight.Commands.CheckHealthCommand;
import com.sma1lboy.ArenaForFight.Commands.GUICommand;
import com.sma1lboy.ArenaForFight.events.EventListener;
import org.bukkit.*;
import org.bukkit.event.Listener;
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
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
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
}

