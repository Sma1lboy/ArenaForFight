package com.sma1lboy.ArenaForFight.sqlite;

import com.sma1lboy.ArenaForFight.ArenaForFight;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLiteGetter {
    private ArenaForFight plugin;

    public SQLiteGetter(ArenaForFight plugin) {
        this.plugin = plugin;
    }

    public void createPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if(!existsInTable(uuid)) {

            String sql = "INSERT INTO users(uuid) VALUES(?)";
            try {
                PreparedStatement ps = this.plugin.sql.connection.prepareStatement(sql);
                //TODO may cause error
                this.plugin.getServer().getConsoleSender().sendMessage(uuid.toString());
                ps.setString(1, uuid.toString());
                ps.execute();
            } catch (SQLException e) {
                //TODO error manager
                e.printStackTrace();
            }
        }

    }
    public void createTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS users(user_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, uuid VARCHAR(100) DEFAULT '' NOT NULL UNIQUE, points INTEGER DEFAULT 0 NOT NULL)";
            PreparedStatement ps = this.plugin.sql.connection.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsInTable(UUID uuid) {
        String sql = "SELECT * FROM users WHERE uuid=?";

        try {
            PreparedStatement ps = this.plugin.sql.connection.prepareStatement(sql);
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            if(results.next()) {
                return true;
            }
            return false;
        }catch (SQLException e ) {
            e.printStackTrace();
        }

        return false;

    }
}
