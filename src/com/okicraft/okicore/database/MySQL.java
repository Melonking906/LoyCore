package com.okicraft.okicore.database;

import com.okicraft.okicore.OkiCore;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQL extends SQL
{
    public MySQL( OkiCore plugin )
    {
        super(plugin);
    }

    protected Connection getNewConnection()
    {

        String host = plugin.getConfig().getString("sql.host");
        String port = plugin.getConfig().getString("sql.port");
        String database = plugin.getConfig().getString("sql.database");
        String user = plugin.getConfig().getString("sql.user");
        String pass = plugin.getConfig().getString("sql.pass");

        try
        {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

            return DriverManager.getConnection( url, user, pass );
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String getName()
    {
        return "MySQL";
    }
}