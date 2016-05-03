package com.okicraft.okicore.database;

import com.okicraft.okicore.OkiCore;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLite extends SQL
{
    private final OkiCore plugin;

    public SQLite( OkiCore plugin )
    {
        super(plugin);

        this.plugin = plugin;
    }

    protected Connection getNewConnection()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");

            return DriverManager.getConnection( "jdbc:sqlite:" + new File( plugin.getDataFolder(), "loycore.db" ).getAbsolutePath() );
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String getName()
    {
        return "SQLite";
    }
}
