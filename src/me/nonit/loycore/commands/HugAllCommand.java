package me.nonit.loycore.commands;

import me.nonit.loycore.LoyCore;
import me.nonit.loycore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HugAllCommand implements CommandExecutor
{
    public HugAllCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if ( ( sender instanceof Player ) )
        {
            if( sender.hasPermission( "loy.hugall" ) )
            {
                if( args.length > 0 )
                {
                    String senderName = ((Player)sender).getDisplayName();

                    for( Player player : Bukkit.getServer().getOnlinePlayers() )
                    {
                        player.sendMessage( ChatColor.GRAY + "❤ " + senderName + ChatColor.GRAY + " hugged " + args[0] + " ❤" );

                        TitleMessage.showMessage( player, "", ChatColor.LIGHT_PURPLE + "❤❤❤ " + senderName + ChatColor.LIGHT_PURPLE + " hugged you! ❤❤❤", 50 );
                    }
                }
                else
                {
                    sender.sendMessage( LoyCore.getPfx() + ChatColor.LIGHT_PURPLE + "Hug everyone with /hugall :D" );
                }
                return true;
            }

        }
        return true;
    }
}
