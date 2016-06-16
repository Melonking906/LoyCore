package com.okicraft.okicore.commands;

import com.okicraft.okicore.OkiCore;
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
        if( ! sender.hasPermission( "oki.kickemall" ) )
        {
            sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "With great power comes... never mind, you ain\'t got the power!" );
            return true;
        }

        for( Player player : Bukkit.getServer().getOnlinePlayers() )
        {
            player.kickPlayer( ChatColor.YELLOW + "Oki is restarting!" + ChatColor.GREEN + " Come back soon :D" );
        }

        System.out.print( "[Oki] All players have been disconnected!" );

        Bukkit.getServer().dispatchCommand( Bukkit.getConsoleSender(), "stop" );

        return true;
    }
}
