package me.nonit.loycore.database;

import me.nonit.loycore.LoyCore;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQL extends SQL
{
    public MySQL( LoyCore plugin )
    {
        super(plugin);
    }

    protected Connection getNewConnection()
    {
        String host = "localhost";
        String port = "3306";
        String database = "loy";
        String user = "loy";
        String pass = "U^8d3;5B3&uCW6Y";

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