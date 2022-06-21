package com.sma1lboy.ArenaForFight;
import com.sma1lboy.ArenaForFight.Commands.CheckHealthCommand;
import com.sma1lboy.ArenaForFight.Commands.GUICommand;
import com.sma1lboy.ArenaForFight.events.EventListener;
import com.sma1lboy.ArenaForFight.sqlite.SQLite;
import com.sma1lboy.ArenaForFight.sqlite.SQLiteGetter;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

/**
 * @author jacksonchen
 */
public class ArenaForFight extends JavaPlugin implements Listener {


    public String language;
    public SQLite sql;
    public SQLiteGetter sqlGetter;

    @Override
    public void onEnable() {
        /*
         * Assign data folder if not exist
         */
        if (!getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        //open the database;
        sql = new SQLite(getDataFolder().getAbsolutePath() + "/server.db");
        sql.connect();
        sqlGetter = new SQLiteGetter(this);
        sqlGetter.createTable();
        if(sql.isConnected()) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[ArenaForFight]: database connceted" );
        }



        saveDefaultConfig();
        reloadConfig();
        saveResource("message.yml", true);
        //setting the language of this plugin
        language = this.getConfig().getString("language");
        String lang = "lang." + language;



        reloadConfig();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getServer().getPluginCommand("gui")).setExecutor(new GUICommand());
        Objects.requireNonNull(getServer().getPluginCommand("healthCheck")).setExecutor(new CheckHealthCommand());

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[ArenaForFight]: " + ChatColor.BLACK +
                this.getConfig().getString("pluginMessage.message"));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[ArenaForFight]: " + ChatColor.BLACK +
                this.getConfig().getString("pluginMessage.pluginEnable"));


    }
    @Override
    public void onDisable() {
        sql.disconnect();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[AFF]:" + "sql is disconnect!");
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ArenaForFight]: " + ChatColor.BLACK +
                this.getConfig().getString("pluginMessage.pluginDisable"));

    }

}

