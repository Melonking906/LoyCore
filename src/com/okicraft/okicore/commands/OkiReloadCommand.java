package com.okicraft.okicore.commands;

import com.okicraft.okicore.JoinLeaveListener;
import com.okicraft.okicore.OkiCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OkiReloadCommand implements CommandExecutor
{
    private OkiCore plugin;

    public OkiReloadCommand(OkiCore plugin )
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( sender.hasPermission( "loy.reload" ) )
        {
            plugin.reloadConfig();
            JoinLeaveListener.reloadMessages( plugin );
            sender.sendMessage( OkiCore.getPfx() + "Oki Messages Reloaded!" );
        }
        return true;
    }
}
