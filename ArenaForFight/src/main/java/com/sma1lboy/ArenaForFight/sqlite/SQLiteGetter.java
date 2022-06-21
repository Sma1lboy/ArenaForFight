package com.sma1lboy.ArenaForFight.sqlite;

import com.sma1lboy.ArenaForFight.ArenaForFight;
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
        String sql = "INSERT INTO users(uuid) VALUES(?)";
        try {
            PreparedStatement ps = this.plugin.sql.getConnection().prepareStatement(sql);
            //TODO may cause error
            ps.setInt(1, Integer.parseInt(uuid.toString()));
        } catch (SQLException e) {
            //TODO error manager
            e.printStackTrace();
        }

    }

    public boolean existsInTable(UUID uuid) {
        return false;
    }
}
