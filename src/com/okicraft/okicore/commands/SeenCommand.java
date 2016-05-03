package com.okicraft.okicore.commands;

import com.okicraft.okicore.OkiCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class SeenCommand implements CommandExecutor
{
    private final OkiCore plugin;

    public SeenCommand( OkiCore plugin )
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if ( ( sender instanceof Player ) )
        {
            if( args.length > 0 )
            {
                String search = args[0];

                if( search.length() < 3 )
                {
                    sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Please search with 3 or more letters!" );
                    return true;
                }

                ArrayList<HashMap<String,String>> data = plugin.db.searchPlayers( search );

                if( data == null )
                {
                    sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Sorry, no players found by the name " + search );
                    return true;
                }

                sender.sendMessage( ChatColor.GRAY + "  *=== " + ChatColor.YELLOW + "Spotted on Loy" + ChatColor.GRAY + " ===*" );
                for( HashMap<String,String> row : data )
                {
                    SimpleDateFormat sqlFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                    SimpleDateFormat printFormat = new SimpleDateFormat( "EEE, d MMM HH:mm" );

                    String name = row.get( "name" );

                    if( Bukkit.getOfflinePlayer( UUID.fromString(row.get( "uuid" )) ).isOnline() )
                    {
                        sender.sendMessage( ChatColor.GREEN + name + ChatColor.GRAY + " is online " + ChatColor.GREEN + "now!" );
                    }
                    else
                    {
                        Date seenDate;
                        try
                        {
                            seenDate = sqlFormat.parse( row.get( "lastonline" ) );
                        }
                        catch( ParseException e )
                        {
                            continue;
                        }

                        String lastseen = printFormat.format( seenDate );

                        sender.sendMessage( ChatColor.GREEN + name + ChatColor.GRAY + " was last seen on " + ChatColor.YELLOW + lastseen + " New York time!" );
                    }
                }
            }
            else
            {
                sender.sendMessage( OkiCore.getPfx() + "Look up a player with /seen <name>!" );
            }
        }
        return true;
    }
}
