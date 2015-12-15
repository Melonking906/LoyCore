package me.nonit.loycore.commands;

import me.nonit.loycore.LoyCore;
import me.nonit.loycore.chat.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PlayerTalkCommand implements CommandExecutor
{
    private ChatListener cl;

    public PlayerTalkCommand(ChatListener cl)
    {
        this.cl = cl;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( ! sender.hasPermission( "loy.playertalk" ) )
        {
            sender.sendMessage( LoyCore.getPfx() + ChatColor.RED + "Sorry you don't have permission for that!" );
            return true;
        }

        if( args.length < 2 )
        {
            sender.sendMessage( LoyCore.getPfx() + "Usage: /pt <name> <message> to speak as a player." );
            return true;
        }

        String name = args[0];
        OfflinePlayer player = Bukkit.getOfflinePlayer( name );
        if( !player.isOnline() )
        {
            sender.sendMessage( LoyCore.getPfx() + ChatColor.RED + "That person does not seem to be online!" );
            return true;
        }

        String msg = "§f";

        args[0] = "";

        for( String word : args )
        {
            if( ! word.equals( "" ) )
            {
                msg = msg + word + " ";
            }
        }

        msg = ChatColor.translateAlternateColorCodes( '&', msg );

        cl.onChat( new AsyncPlayerChatEvent( true, (Player) player, msg, new HashSet<>( Bukkit.getOnlinePlayers() ) ) );

        return true;
    }
}