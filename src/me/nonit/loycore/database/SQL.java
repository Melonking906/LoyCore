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
    private SimpleDateFormat sqlDateFormat;

    protected LoyCore plugin;

    public SQL( LoyCore plugin )
    {
        this.plugin = plugin;
        this.sqlDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

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

            plugin.getLogger().warning( "The Query for the error was:" + sql );
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
                for( int i = 1; i <= columns; ++i )
                {
                    String columnData = null;
                    if ( set.getObject( i ) != null )
                    {
                        columnData = set.getObject( i ).toString();
                    }

                    row.put( md.getColumnName( i ), columnData );
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
        Date date = new Date();
        return sqlDateFormat.format( date );
    }

    //+++ Death SQL +++

    public ArrayList<DeadPlayer> getDeadPlayers()
    {
        Date now = new Date();
        ArrayList<DeadPlayer> deadPlayers = new ArrayList<>();

        ArrayList<HashMap<String,String>> data = query( "SELECT uuid, lastdeath FROM loy_players WHERE deaths > 0;", true );
        if( data == null )
        {
            return deadPlayers;
        }

        for( HashMap<String,String> row : data )
        {
            UUID playerUUID;
            Date deathDate;

            try
            {
                playerUUID = UUID.fromString(row.get("uuid"));
                deathDate = sqlDateFormat.parse(row.get("lastdeath"));
            }
            catch (Exception x)
            {
                continue;
            }

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(deathDate);
            calendar.add(Calendar.MINUTE, 5);

            Date dateOfAwake = calendar.getTime();

            if (now.before(dateOfAwake))
            {
                deadPlayers.add( new DeadPlayer( playerUUID, dateOfAwake ) );
            }
            else
            {
                resetPlayerDeath(playerUUID);
            }
        }

        return deadPlayers;
    }

    public void updatePlayerDeath( Player player )
    {
        String uuid = player.getUniqueId().toString();

        ArrayList<HashMap<String,String>> data = query("SELECT deaths FROM loy_players WHERE uuid = '"+uuid+"'", true);
        if( data == null )
        {
            return;
        }

        int deaths;

        try
        {
            deaths = Integer.parseInt(data.get(0).get("deaths"));
        }
        catch( Exception e )
        {
            return;
        }

        deaths++;
        String dateOfDeath = getNowInSQLFormat();

        query("UPDATE loy_players SET deaths = "+deaths+", lastdeath = '"+dateOfDeath+"' WHERE uuid = '"+uuid+"'", false);
    }

    public void resetPlayerDeath( UUID uuid )
    {
        query("UPDATE loy_players SET deaths = 0 WHERE uuid = '"+uuid.toString()+"'", false);
    }

    public void resetAllPlayerDeaths()
    {
        query("UPDATE loy_players SET deaths = 0;", false);
    }

    public class DeadPlayer
    {
        private UUID uuid;
        private Date dateOfAwake;

        public DeadPlayer(UUID uuid, Date dateOfAwake)
        {
            this.uuid = uuid;
            this.dateOfAwake = dateOfAwake;
        }

        public UUID getUuid()
        {
            return uuid;
        }

        public Date getDateOfAwake()
        {
            return dateOfAwake;
        }
    }
}
