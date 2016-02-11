package me.nonit.loycore.death;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResurrectCommand implements CommandExecutor
{
    private Death death;

    public ResurrectCommand( Death death )
    {
        this.death = death;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if( !sender.hasPermission( "loy.resurrect" ) )
        {
            sender.sendMessage(ChatColor.GRAY + "You don't hold such power...");
            return true;
        }

        if( args.length < 1 )
        {
            sender.sendMessage("Use: /resurrect <name>");
            return true;
        }

        String name = args[0];

        if( name.equals("all") || name.equals( "*" ) )
        {
            death.getDB().resetAllPlayerDeaths();
            death.refreshDeadPlayers();

            sender.sendMessage("All player deaths reset");
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(name);

        if( !player.hasPlayedBefore() )
        {
            sender.sendMessage("That person has not played before...");
            return true;
        }

        death.getDB().resetPlayerDeath( player.getUniqueId() );
        death.refreshDeadPlayers();

        sender.sendMessage("Player has been resurrected!");

        return true;
    }
}
