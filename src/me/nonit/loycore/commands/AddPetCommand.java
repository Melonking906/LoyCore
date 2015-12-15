package me.nonit.loycore.commands;

import me.nonit.loycore.LoyCore;
import me.nonit.loycore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddPetCommand implements CommandExecutor
{
    public AddPetCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( sender.hasPermission( "loy.addpet" ) )
        {
            if( args.length > 0 )
            {
                OfflinePlayer player = Bukkit.getOfflinePlayer( args[0] );

                if( !player.hasPlayedBefore() )
                {
                    sender.sendMessage( LoyCore.getPfx() + ChatColor.RED + "That player has never played before..." );
                    return true;
                }

                LoyCore.permission.playerAdd( null, player, "permpack.pet" );

                LoyCore.permission.playerAdd( null, player, "echopet.pet.type.chicken" );
                LoyCore.permission.playerAdd( null, player, "echopet.pet.type.chicken.*" );

                LoyCore.permission.playerAdd( null, player, "echopet.pet.type.wolf" );
                LoyCore.permission.playerAdd( null, player, "echopet.pet.type.wolf.*" );

                LoyCore.permission.playerAdd( null, player, "echopet.pet.type.ocelot" );
                LoyCore.permission.playerAdd( null, player, "echopet.pet.type.ocelot.*" );

                sender.sendMessage( LoyCore.getPfx() + args[0] + " has been granted pet permissions!" );

                if( player.isOnline() )
                {
                    TitleMessage.showMessage( player.getPlayer(), "", ChatColor.GREEN + "You can now use pets! /pet !!", 60 );
                }
            }
            else
            {
                sender.sendMessage( LoyCore.getPfx() + ChatColor.LIGHT_PURPLE + "Add pet permissions /addpet <PlayerName> :D" );
            }
        }

        return true;
    }
}
