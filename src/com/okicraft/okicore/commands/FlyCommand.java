package com.okicraft.okicore.commands;

import com.okicraft.okicore.OkiCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor
{
    public FlyCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if ( ( sender instanceof Player ) )
        {
            if( sender.hasPermission( "loy.fly" ) )
            {
                Player p = (Player) sender;

                if( args.length == 0 )
                {
                    togglePlayerFly( p );
                }
                else if ( sender.hasPermission( "loy.fly.other" ) )
                {
                    Player other = Bukkit.getPlayer( args[0] );

                    if( other == null )
                    {
                        p.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Sorry that player is not online :(" );
                        return true;
                    }

                    togglePlayerFly( other );

                    p.sendMessage( OkiCore.getPfx() + "Flight enabled for " + other.getDisplayName() );
                }
                return true;
            }
            else
            {
                sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Sorry, no permission to fly!" );
            }
        }
        return true;
    }

    private void togglePlayerFly( Player p )
    {
        if( p.getAllowFlight() )
        {
            p.setAllowFlight( false );
            p.sendMessage( OkiCore.getPfx() + ChatColor.RED + "++ Flight Disabled ++" );
        }
        else
        {
            p.setAllowFlight( true );
            p.sendMessage( OkiCore.getPfx() + "++ Flight Enabled ++" );
        }
    }
}
