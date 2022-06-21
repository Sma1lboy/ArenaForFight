package com.sma1lboy.ArenaForFight;
import com.sma1lboy.ArenaForFight.Commands.CheckHealthCommand;
import com.sma1lboy.ArenaForFight.Commands.GUICommand;
import com.sma1lboy.ArenaForFight.events.EventListener;
import com.sma1lboy.ArenaForFight.sqlite.SQLite;
import com.sma1lboy.ArenaForFight.sqlite.SQLiteGetter;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ArenaForFight extends JavaPlugin implements Listener {


    public String language;
    public SQLite sql;
    public SQLiteGetter sqlGetter;

    @Override
    public void onEnable() {
        //set up mysql
        //FIXME: haven't create database yet

        File dataFolder = getDataFolder();
        File file = new File(dataFolder.getAbsolutePath() + "/server.db");

        if(!file.exists()) {
            sql = new SQLite(dataFolder.getAbsolutePath() + "/server.db");
            sqlGetter = new SQLiteGetter(this);
            sql.connect();
            sql.createTable();
        }

        if(sql.isConnected()) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[AFF]:" + "sql is connected!");
        }

        saveDefaultConfig();
        reloadConfig();
        //setting the language of this plugin
        language = "lang." + this.getConfig().getString("language");
        /*
        save the messaeg.yml
         */
        saveResource("message.yml", true);
        this.getResource("message.yml");



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
        sql.disconnect();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[AFF]:" + "sql is disconnect!");
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ArenaForFight]: " + ChatColor.WHITE +
                this.getConfig().getString(language + "." + "pluginDisable"));

    }

}

