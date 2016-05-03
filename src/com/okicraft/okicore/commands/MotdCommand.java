package com.okicraft.okicore.commands;

import com.okicraft.okicore.JoinLeaveListener;
import com.okicraft.okicore.JoinLeaveListener;
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
