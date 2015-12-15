package me.nonit.loycore.database;

import me.nonit.loycore.LoyCore;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public abstract class SQL
{
    private Connection connection;

    protected LoyCore plugin;

    public SQL( LoyCore plugin )
    {
        this.plugin = plugin;

        plugin.getServer().getScheduler().runTaskTimerAsynchronously( plugin, new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if( connection != null && ! connection.isClosed() )
                    {
                        connection.createStatement().execute( "/* ping */ SELECT 1" );
                    }
                }
                catch( SQLException e )
                {
                    connection = getNewConnection();
                }
            }
        }, 60 * 20, 60 * 20 );
    }

    protected abstract Connection getNewConnection();

    protected abstract String getName();

    public String getConfigName()
    {
        return getName().toLowerCase().replace(" ", "");
    }

    private ArrayList<HashMap<String,String>> query( String sql, boolean hasReturn )
    {
        if( ! checkConnection() )
        {
            plugin.getLogger().info( "Error with database" );
            return null;
        }

        PreparedStatement statement;
        try
        {
            statement = connection.prepareStatement( sql );

            if( ! hasReturn )
            {
                statement.execute();
                return null;
            }

            ResultSet set = statement.executeQuery();

            ResultSetMetaData md = set.getMetaData();
            int columns = md.getColumnCount();

            ArrayList<HashMap<String,String>> list = new ArrayList<>( 50 );

            while( set.next() )
            {
                HashMap<String,String> row = new HashMap<>( columns );
                for( int i = 1; i < columns; ++i )
                {
                    row.put( md.getColumnName( i ), set.getObject( i ).toString() );
                }
                list.add( row );
            }

            if( list.isEmpty() )
            {
                return null;
            }

            return list;
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }

        return null;
    }

    public boolean checkConnection()
    {
        try
        {
            if( connection == null || connection.isClosed() )
            {
                connection = getNewConnection();

                if( connection == null || connection.isClosed() )
                {
                    return false;
                }
            }
        }
        catch( SQLException e )
        {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public void disconnect()
    {
        try
        {
            if( connection != null )
            {
                connection.close();
            }
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }

    public ArrayList<HashMap<String,String>> searchPlayers( String search )
    {
        return query( "SELECT * FROM loy_players WHERE LOWER(name) LIKE LOWER('%"+search+"%')", true );
    }

    public HashMap<String,String> getPlayer( String uuid )
    {
        ArrayList<HashMap<String,String>> data = query( "SELECT * FROM loy_players WHERE uuid = '"+uuid+"'", true );

        if( data == null )
        {
            return null;
        }
        return data.get( 0 );
    }

    public ArrayList<HashMap<String,String>> getAllPlayers()
    {
        return query( "SELECT * FROM loy_players", true );
    }

    public void setPromotedTime( Player player )
    {
        String uuid = player.getUniqueId().toString();

        query( "UPDATE loy_players SET firstonline = '"+getNowInSQLFormat()+"' WHERE uuid = '"+uuid+"'", false );
    }

    public void updatePlayer( Player player )
    {
        String uuid = player.getUniqueId().toString();
        String name = player.getName();

        if( query( "SELECT uuid FROM loy_players WHERE uuid = '"+uuid+"';", true ) != null )
        {
            query( "UPDATE loy_players SET name = '"+name+"', lastonline = '"+getNowInSQLFormat()+"' WHERE uuid = '"+uuid+"'", false );
        }
        else
        {
            query( "INSERT INTO loy_players (uuid, name, lastonline) VALUES ('"+uuid+"', '"+name+"', '" +getNowInSQLFormat()+"');", false );
        }
    }

    private String getNowInSQLFormat()
    {
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date = new Date();

        return format.format( date );
    }
}
