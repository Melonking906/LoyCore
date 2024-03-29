package com.okicraft.okicore.commands;

import com.okicraft.okicore.EmeraldEcon;
import com.okicraft.okicore.OkiCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EmeraldsCommand implements CommandExecutor
{
    public EmeraldsCommand() {}

    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args )
    {
        if ( args.length < 1 )
        {
            if ( !(sender instanceof Player) )
            {
                sender.sendMessage( "Please use /"+command + " <name>" );
                return true;
            }

            int balance = EmeraldEcon.getBalance( (Player) sender );
            if ( balance < 1 )
            {
                sender.sendMessage( OkiCore.getPfx() + "You don't have any emeralds in your inventory!" );
                return true;
            }

            sender.sendMessage( OkiCore.getPfx() + "You have " + ChatColor.YELLOW + balance + ChatColor.GREEN + " emeralds in your inventory!" );
            return true;
        }
        else
        {
            if ( !sender.hasPermission( "loy.emeralds.other" ) )
            {
                sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "You can't see into someones pocket!" );
                return true;
            }

            String name = args[0];
            Player player = Bukkit.getPlayer( name );
            if ( player == null || !player.hasPlayedBefore() || !player.isOnline() )
            {
                sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Sorry, that player could not be found :(" );
                return true;
            }

            int balance = EmeraldEcon.getBalance( player );
            sender.sendMessage( OkiCore.getPfx() + player.getDisplayName() + " has " + balance + " emeralds in their inventory!" );
            return true;
        }
    }
}
