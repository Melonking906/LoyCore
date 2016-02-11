package me.nonit.loycore.death;

import me.nonit.loycore.LoyCore;
import me.nonit.loycore.database.SQL;

import java.util.ArrayList;
import java.util.List;

public class Death
{
    private LoyCore plugin;
    private List<SQL.DeadPlayer> deadPlayers;

    public Death( LoyCore plugin )
    {
        this.deadPlayers = new ArrayList<>();
        this.plugin = plugin;
    }

    public void refreshDeadPlayers()
    {
        deadPlayers = plugin.db.getDeadPlayers();
    }

    public List<SQL.DeadPlayer> getDeadPlayers()
    {
        return deadPlayers;
    }

    public SQL getDB()
    {
        return plugin.db;
    }
}