package com.okicraft.okicore.commands;

import com.okicraft.okicore.OkiCore;
import com.okicraft.okicore.TitleMessage;
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
        if ( ( sender instanceof Player ) ) {
            if (((Player) sender).getWorld().getName().equals("Spore")) {
                if (sender.hasPermission("oki.fly")) {
                    Player p = (Player) sender;

                    if (args.length == 0) {
                        togglePlayerFly(p);
                    } else if (sender.hasPermission("oki.fly.other")) {
                        Player other = Bukkit.getPlayer(args[0]);

                        if (other == null) {
                            OkiCore.playErrorSound( (Player) sender );
                            p.sendMessage(OkiCore.getPfx() + ChatColor.RED + "Sorry, that player is not online :(");
                            return true;
                        }

                        togglePlayerFly(other);

                        p.sendMessage(OkiCore.getPfx() + "Flight enabled for " + other.getDisplayName());
                    }
                    return true;
                } else {

                    OkiCore.playErrorSound( (Player) sender );
                    sender.sendMessage(OkiCore.getPfx() + "You don't have permission to fly! :O Let an admin know please! ;3");

                }
            }
            else
            {

                if( sender.hasPermission( "oki.fly.anyworld") )
                {

                    togglePlayerFly( (Player) sender );

                }
                else
                {
                    ((Player) sender).setFlying( false );
                    ((Player) sender).setAllowFlight( false );
                    OkiCore.playErrorSound( (Player) sender );
                    sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Sorry, if you're not a Phoenix, you cannot fly in this world! 3:");

                }

            }
        }
        return true;
    }

    private void togglePlayerFly( Player p )
    {
        if( p.getAllowFlight() )
        {
            p.setAllowFlight( false );
            TitleMessage.showMessage( p, ChatColor.RED + "++ Flight Disabled ++", "" , 6, 3, 3 );
            p.sendMessage( OkiCore.getPfx() + ChatColor.RED + "++ Flight Disabled ++" );
        }
        else
        {
            p.setAllowFlight( true );
            TitleMessage.showMessage( p, ChatColor.GREEN + "++ Flight Enabled ++", "", 6, 3, 3 );
            p.sendMessage( OkiCore.getPfx() + "++ Flight Enabled ++");
        }
    }
}
