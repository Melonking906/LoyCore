package com.okicraft.okicore.chat;

import com.okicraft.okicore.OkiCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ChatCommand implements CommandExecutor
{
    private ChannelStore cs;
    private final HashMap<Character,String> knownChannels;

    public ChatCommand( ChannelStore cs )
    {
        this.cs = cs;
        this.knownChannels = new HashMap<>();
        knownChannels.put( 'g', "Global" );
        knownChannels.put( 'l', "Local" );
        knownChannels.put( 's', "Staff" );
        knownChannels.put( 'w', "World-Specific Chat");
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( !(sender instanceof Player) )
        {
            sender.sendMessage( "Only a player can do this!" );
            return true;
        }

        Player p = (Player) sender;

        if( args.length > 0 )
        {
            char channelRequested = args[0].charAt( 0 );

            if( ! knownChannels.containsKey( channelRequested ) )
            {
                p.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Hmm.. that's not a channel.. try /c for a list!" );
                return true;
            }

            if( ! p.hasPermission( "loy.chat." + knownChannels.get( channelRequested ).toLowerCase() ) )
            {
                p.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Sorry! you don't have permission for that channel!" );
                return true;
            }

            if( args.length == 1 )
            {
                cs.setPlayerChannel( p, channelRequested );
                p.sendMessage( OkiCore.getPfx() + "You switched into " + ChatColor.YELLOW + knownChannels.get( channelRequested ) + ChatColor.GREEN + " chat!" );
            }
            else
            {
                String msg = "";
                for ( int i = 1; i < args.length ; i++ )
                {
                    msg += args[i] + " ";
                }

                char playerChannel = cs.getPlayerChannel( p );

                cs.setPlayerChannel( p, channelRequested );
                p.chat( msg );
                cs.setPlayerChannel( p, playerChannel );
            }
        }
        else
        {
            p.sendMessage( OkiCore.getPfx() + "Change your chat channel with " + ChatColor.YELLOW + "/c <channel> (msg)" );
            p.sendMessage( ChatColor.YELLOW + "----" + ChatColor.GRAY + "Known Channels" +  ChatColor.YELLOW + "----" );
            for( Map.Entry<Character,String> channel : knownChannels.entrySet() )
            {
                if( p.hasPermission( "loy.chat." + channel.getValue().toLowerCase() ) )
                {
                    p.sendMessage( ChatColor.YELLOW + "* " + channel.getKey() + ChatColor.GRAY + " - " + channel.getValue() + " chat!" );
                }
            }
        }

        return true;
    }
}
