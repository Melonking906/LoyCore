package com.okicraft.okicore.prefix;

import com.okicraft.okicore.OkiCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PfxTokenCommand implements CommandExecutor
{
    public PfxTokenCommand()
    {

    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args )
    {
        if( args.length < 1 )
        {
            sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Please use /prefix <prefix> (player)" );
            return true;
        }

        if( !sender.hasPermission( "oki.prefix" ) )
        {
            sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "You don't have perms for that!" );
            return true;
        }

        if( args.length < 2 && !(sender instanceof Player) )
        {
            sender.sendMessage( "Please include a player: /prefix <prefix> <name>" );
            return true;
        }

        String prefix = args[0];
        Player reciver;

        if( args.length == 1 )
        {
            reciver = (Player) sender;
        }
        else
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer( args[1] );

            if( !offlinePlayer.isOnline() )
            {
                sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + args[1] + " does not seem to be online to get a token!" );
                return true;
            }

            reciver = offlinePlayer.getPlayer();
        }

        //Spawn Token
        PlayerInventory inventory = reciver.getInventory();

        String displayName = ChatColor.GOLD + "Prefix Token:" + ChatColor.WHITE + " " + prefix;
        List<String> lore = new ArrayList<>();
        lore.add( ChatColor.YELLOW + "Sets your prefix" );
        lore.add( ChatColor.GREEN + "Right click to set!" );
        lore.add( ChatColor.GRAY + prefix );

        ItemStack token = new ItemStack( Material.PAPER, 1 );
        ItemMeta im = token.getItemMeta();
        im.setDisplayName( displayName );
        im.setLore( lore );
        token.setItemMeta( im );

        if( inventory.firstEmpty() == -1 )
        {
            reciver.getWorld().dropItem( reciver.getLocation(), token );
        }
        else
        {
            inventory.addItem( token );
        }

        reciver.sendMessage( OkiCore.getPfx() + "You got a prefix token for " + ChatColor.GRAY + prefix + ChatColor.GREEN + "!" );

        return true;
    }
}
