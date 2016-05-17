package com.okicraft.okicore.commands;

import com.okicraft.okicore.OkiCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class UnSignCommand implements CommandExecutor
{
    public UnSignCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( sender instanceof Player )
        {
            Player player = (Player) sender;
            ItemStack is = player.getInventory().getItemInMainHand();

            if( player.hasPermission( "oki.unsign" ) )
            {
                if( is.getType() == Material.WRITTEN_BOOK )
                {
                    if( is.hasItemMeta() )
                    {
                        ItemMeta meta = is.getItemMeta();
                        if( ( meta instanceof BookMeta ) )
                        {
                            BookMeta bookMeta = ( BookMeta ) meta;
                            if( ( bookMeta.hasAuthor() ) && ( bookMeta.getAuthor().equalsIgnoreCase( player.getName() ) || player.hasPermission( "oki.unsign.other" ) ) )
                            {
                                BookMeta newMeta = bookMeta.clone();
                                ItemStack newIs = new ItemStack( Material.BOOK_AND_QUILL );
                                newIs.setItemMeta( newMeta );
                                player.getInventory().setItemInMainHand( newIs );

                                player.sendMessage( OkiCore.getPfx() + ChatColor.GREEN + "Unsigned \"" + ChatColor.WHITE + bookMeta.getTitle() + ChatColor.GREEN + "\"!" );
                            }
                            else
                            {
                                player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "You cannot unsign other people\'s books!" );
                            }
                        }
                    }
                }
            }
            else
            {
                player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "No permission :(" );
            }
        }

        return true;
    }
}
