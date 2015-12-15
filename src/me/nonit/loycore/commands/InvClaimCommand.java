package me.nonit.loycore.commands;

import me.nonit.loycore.InvSaverListener;
import me.nonit.loycore.LoyCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InvClaimCommand implements CommandExecutor
{
    private InvSaverListener invs;

    public InvClaimCommand( InvSaverListener invs )
    {
        this.invs = invs;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( ( sender instanceof Player ) )
        {
            if( sender.hasPermission( "loy.invclaim" ) )
            {
                Player p = (Player) sender;
                InvSaverListener.Drop drop = invs.getInv( p.getUniqueId() );

                if( drop == null )
                {
                    p.sendMessage( LoyCore.getPfx() + ChatColor.RED + "Looks like there are no invs saved under your name :(" );
                    return true;
                }

                if( LoyCore.economy.getBalance( p ) < invs.getClaimCost() )
                {
                    p.sendMessage( LoyCore.getPfx() + ChatColor.RED + "It takes " + ChatColor.WHITE + LoyCore.economy.format( invs.getClaimCost() ) + ChatColor.RED + " to claim your inv, you only have " + ChatColor.WHITE + LoyCore.economy.format( LoyCore.economy.getBalance( p ) ) + ChatColor.RED + " :(" );
                    return true;
                }

                invs.removeInv( p.getUniqueId() );
                ItemStack[] oldInv = p.getInventory().getContents();

                p.getInventory().setArmorContents( drop.getArmor() );
                p.getInventory().setContents( drop.getContents() );
                p.setExp( drop.getExp() );

                LoyCore.economy.withdrawPlayer( p, invs.getClaimCost() );
                LoyCore.economy.depositPlayer( "IoCo", invs.getClaimCost() );

                p.sendMessage( LoyCore.getPfx() + "You recovered your inventory for " + ChatColor.YELLOW + LoyCore.economy.format( invs.getClaimCost() ) + ChatColor.GREEN + " !" );

                for( ItemStack item : oldInv )
                {
                    if( item != null && ! item.getType().equals( Material.AIR ) )
                    {
                        p.getWorld().dropItem( p.getLocation(), item );
                    }
                }
            }
        }

        return true;
    }
}