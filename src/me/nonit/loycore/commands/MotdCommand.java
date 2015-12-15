package me.nonit.loycore.commands;

import me.nonit.loycore.JoinLeaveListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MotdCommand implements CommandExecutor
{
    public MotdCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( sender.hasPermission( "loy.motd" ) )
        {
            JoinLeaveListener.sendMotd( (Player) sender );
        }
        return true;
    }
}
