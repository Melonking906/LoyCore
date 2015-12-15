package me.nonit.loycore.commands;

import me.nonit.loycore.JoinLeaveListener;
import me.nonit.loycore.LoyCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LoyReloadCommand implements CommandExecutor
{
    private LoyCore plugin;

    public LoyReloadCommand( LoyCore plugin )
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
            sender.sendMessage( "Loy Messages Reloaded!!" );
        }
        return true;
    }
}
