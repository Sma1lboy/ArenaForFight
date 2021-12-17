package com.sma1lboy.ArenaForFight;
import com.sma1lboy.ArenaForFight.Commands.CheckHealthCommand;
import com.sma1lboy.ArenaForFight.Commands.GUICommand;
import com.sma1lboy.ArenaForFight.SQL.MySQL;
import com.sma1lboy.ArenaForFight.SQL.SQLGetter;
import com.sma1lboy.ArenaForFight.events.EventListener;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;
import java.util.*;

public class ArenaForFight extends JavaPlugin implements Listener {

    public MySQL SQL;
    public SQLGetter data;
    public String language;

    @Override
    public void onEnable() {
        //set up mysql
        //FIXME: haven't create database yet
        this.SQL = new MySQL();
        data = new SQLGetter(this);

        saveDefaultConfig();
        reloadConfig();
        //setting the language of this plugin
        language = "lang." + this.getConfig().getString("language");

        try {
            this.SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            //e.printStackTrace();
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ArenaForFight]: "
                    + this.getConfig().getString("lang.enUS.databaseConnect"));
        }

        if(this.SQL.isConnected())  {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[ArenaForFight]: "
                    + this.getConfig().getString(language + ".databaseDisconnect"));
            data.createTable();
        }
        reloadConfig();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getServer().getPluginCommand("gui")).setExecutor(new GUICommand());
        getServer().getPluginCommand("healthCheck").setExecutor(new CheckHealthCommand());

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[ArenaForFight]: " + ChatColor.WHITE +
                this.getConfig().getString(language + "." + "message"));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[ArenaForFight]: " + ChatColor.WHITE +
                this.getConfig().getString(language + "." + "pluginEnable"));
    }
    @Override
    public void onDisable() {

        SQL.disconnect();
        if(this.SQL.isConnected())  {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Database is disconnected!");
        }
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ArenaForFight]: " + ChatColor.WHITE +
                this.getConfig().getString(language + "." + "pluginDisable"));

    }
}

