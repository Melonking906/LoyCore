package me.nonit.loycore.commands;

import me.nonit.loycore.LoyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickEmAllCommand implements CommandExecutor
{
    public KickEmAllCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( ! sender.hasPermission( "loy.kickemall" ) )
        {
            sender.sendMessage( LoyCore.getPfx() + ChatColor.RED + "With great power comes.. never mind you aint got the power!" );
            return true;
        }

        for( Player player : Bukkit.getServer().getOnlinePlayers() )
        {
            player.kickPlayer( ChatColor.YELLOW + "LoyLoy is restarting!" + ChatColor.GREEN + " Come back soon :D" );
        }

        System.out.print( "[Loy] All players have been disconnected!" );

        Bukkit.getServer().dispatchCommand( Bukkit.getConsoleSender(), "stop" );

        return true;
    }
}
