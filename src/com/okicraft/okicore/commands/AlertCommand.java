package com.okicraft.okicore.commands;

import com.okicraft.okicore.OkiCore;
import com.okicraft.okicore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertCommand implements CommandExecutor
{
    public AlertCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( sender.hasPermission( "loy.alert" ) )
        {
            if( args.length >= 1 )
            {
                String message = "";
                for( String word : args )
                {
                    message += word + " ";
                }
                message = message.trim();
                message = ChatColor.translateAlternateColorCodes( '&', message );

                for( Player player : Bukkit.getServer().getOnlinePlayers() )
                {
                    message = message.replace( "%player%", player.getDisplayName() );

                    TitleMessage.showMessage( player, "", message, 120 );
                }
            }
            else
            {
                sender.sendMessage( OkiCore.getPfx() + "/alert <message>" );
            }
        }
        else
        {
            sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Sorry no permission!" );
        }
        return true;
    }
}
