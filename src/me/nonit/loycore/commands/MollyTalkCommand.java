package me.nonit.loycore.commands;

import me.nonit.loycore.LoyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class MollyTalkCommand implements CommandExecutor
{
    public MollyTalkCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( ! sender.hasPermission( "loy.mollytalk" ) )
        {
            sender.sendMessage( LoyCore.getPfx() + ChatColor.RED + "Sorry you don't have permission for that!" );
            return true;
        }

        if( args.length < 1 )
        {
            sender.sendMessage( LoyCore.getPfx() + "Usage: /mt <message> to speak as molly." );
            return true;
        }

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        String msg = "";

        for( String word : args )
        {
            msg = msg + word + " ";
        }

        msg = LoyCore.getMol() + msg;
        msg = ChatColor.translateAlternateColorCodes( '&', msg );

        for( Player player : players )
        {
            player.sendMessage( msg.replace( "%player%", player.getDisplayName() ) );
        }

        return true;
    }
}