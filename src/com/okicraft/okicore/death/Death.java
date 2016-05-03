package com.okicraft.okicore.death;

import com.okicraft.okicore.OkiCore;
import com.okicraft.okicore.database.SQL;

import java.util.ArrayList;
import java.util.List;

public class Death
{
    private OkiCore plugin;
    private List<SQL.DeadPlayer> deadPlayers;

    public Death( OkiCore plugin )
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