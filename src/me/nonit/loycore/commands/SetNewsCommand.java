package me.nonit.loycore.commands;

import me.nonit.loycore.LoyCore;
import me.nonit.loycore.JoinLeaveListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetNewsCommand implements CommandExecutor
{
    private LoyCore plugin;

    public SetNewsCommand( LoyCore plugin )
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if ( sender.hasPermission("loy.setnews") )
        {
            if( args.length > 1 )
            {
                //Get the msg for args
                String msg = "";
                for ( int i = 1; i < args.length; i++ )
                {
                    msg += args[i] + " ";
                }

                if( args[0].equals( "1" ) )
                {
                    sender.sendMessage( LoyCore.getPfx() + "News line 1 has been updated!" );
                    plugin.getConfig().set( "newsLine1", msg );

                    JoinLeaveListener.reloadMessages( plugin );
                }
                else if( args[0].equals( "2" ) )
                {
                    sender.sendMessage( LoyCore.getPfx() + "News line 2 has been updated!" );
                    plugin.getConfig().set( "newsLine2", msg );

                    JoinLeaveListener.reloadMessages( plugin );
                }
                else
                {
                    sender.sendMessage( LoyCore.getPfx() + "Do /setnews <1-2> <message> to set news!" );
                }
            }
            else
            {
                sender.sendMessage( LoyCore.getPfx() + "Do /setnews <1-2> <message> to set news!" );
            }
        }
        else
        {
            sender.sendMessage( LoyCore.getPfx() + "No permission :(" );
        }

        return true;
    }
}
