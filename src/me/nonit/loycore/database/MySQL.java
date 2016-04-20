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
        String database = "mc28126";
        String user = "mc28126";
        String pass = "bb08be6108";
// mysql://localhost:3306/mc28126
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