package com.sma1lboy.ArenaForFight;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGetter {
    private ArenaForFight plugin;

    //instance arena for fight
    public SQLGetter(ArenaForFight plugin) {
        this.plugin = plugin;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS wonpoints " +
                    "(NAME VARCHAR(100), UUID VARCHAR(100), POINTS INT(100), PRIMARY KEY (NAME))");
            ps.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            if(!existsInTable(uuid)) {
                PreparedStatement ps  = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO wonpoints" +
                        " (NAME,UUID) VALUES (?,?)");
                ps.setString(1, player.getName());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsInTable(UUID uuid) {
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement(
                    "SELECT * FROM wonpoints WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet result = ps.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addPoints (UUID uuid, int points) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement(
                    "UPDATE wonpoints SET POINTS=? WHERE UUID=?");
            ps.setInt(1, getPoints(uuid) + points);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getPoints(UUID uuid ){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement(
                    "SELECT POINTS FROM wonpoints WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int points = 0;
            if(rs.next()) {
                points = rs.getInt("POINTS");
                return points;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
