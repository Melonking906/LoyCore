package com.okicraft.okicore.commands;

import com.okicraft.okicore.EmeraldEcon;
import com.okicraft.okicore.OkiCore;
import com.okicraft.okicore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SendCommand implements CommandExecutor
{
    private static final int COST = 1;

    public SendCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if ( ( sender instanceof Player ) )
        {
            Player s = (Player) sender;

            if( s.hasPermission( "loy.send" ) )
            {
                if( args.length > 0 )
                {
                    if ( EmeraldEcon.getBalance( s ) < COST )
                    {
                        s.sendMessage( OkiCore.getPfx() + ChatColor.RED + "You cant afford that! Its " + COST + " emerald to send an item." );
                        return true;
                    }

                    String sName = s.getDisplayName();

                    if( args[0].equals( s.getName() ) )
                    {
                        s.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Lets save the bother and not send it at all xD" );
                        return true;
                    }

                    if( Bukkit.getOfflinePlayer( args[0] ).isOnline() )
                    {
                        Player r = Bukkit.getServer().getPlayer( args[0] );

                        if( r.getInventory().firstEmpty() == -1 )
                        {
                            s.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Aww, looks like " + r.getDisplayName() + ChatColor.RED + "'s inventory is full, cant send!" );
                            r.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Aww, " + sName + ChatColor.RED + " tried to send an item, but your inventory is full!" );
                            return true;
                        }

                        ItemStack item = s.getInventory().getItemInMainHand();

                        if( item.getType().equals( Material.AIR ) )
                        {
                            sender.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Umm... You cant send air!" );
                            return true;
                        }

                        s.getInventory().setItemInMainHand( new ItemStack( Material.AIR ) );

                        r.getInventory().addItem( item );
                        TitleMessage.showMessage( r, "", ChatColor.GREEN + "You got an item from " + ChatColor.YELLOW + sName + ChatColor.GREEN + ", check your inv!", 60 );

                        EmeraldEcon.removeEmeralds( s, COST );

                        //s.sendMessage( OkiCore.getPfx() + "Your item was sent to " + r.getDisplayName() + ChatColor.GREEN + " for " + OkiCore.economy.format( COST ) + " :D" );
                        s.sendMessage( OkiCore.getPfx() + "Your item was sent to " + r.getDisplayName() + ChatColor.GREEN + " :D" );
                    }
                    else
                    {
                        s.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Oops, " + args[0] + " doesn't seem to be online, cant send!" );
                        return true;
                    }
                }
                else
                {
                    //s.sendMessage( OkiCore.getPfx() + "Send items to people for only " + OkiCore.economy.format( COST ) + "! /" + label + " <name>" );
                    s.sendMessage( OkiCore.getPfx() + "Send items to people! /" + label + " <name>" );
                }
                return true;
            }
        }
        return true;
    }
}
