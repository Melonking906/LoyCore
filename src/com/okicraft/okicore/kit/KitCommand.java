/*

package com.okicraft.okicore.kit;

import com.okicraft.okicore.OkiCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


*/
/**
 * Created by skitt on 5/16/2016.
 *//*


public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if( (sender.hasPermission( "oki.starterkit" )))
        {
            if(!(sender.hasPermission( "oki.starterkit.other" ))) {
                if (args.length != 0) {

                    sender.sendMessage(OkiCore.getPfx() + ChatColor.RED + "Totes Errorz!  This command doesn't need anything after it!");
                    sender.sendMessage(OkiCore.getPfx() + "Get new starter kit with " + ChatColor.WHITE + "/newkit" + ChatColor.GREEN + "!");
                    sender.sendMessage(OkiCore.getPfx() + ChatColor.RED + "WARNING:  THIS WILL " + ChatColor.BOLD + "REPLACE" + ChatColor.RESET + "YOUR CURRENT INVENTORY WITH THE KIT." );
                }
            }
            else
            {
                if(args.length != 1) {
                    sender.sendMessage(OkiCore.getPfx() + "Give someone else a new starter kit with " + ChatColor.WHITE + "/newkit <player>" + ChatColor.GREEN + "!");
                    sender.sendMessage(OkiCore.getPfx() + ChatColor.RED + "WARNING:  THIS WILL " + ChatColor.BOLD + "REPLACE" + ChatColor.RESET + "THE PLAYER'S CURRENT INVENTORY WITH THE KIT." );
                }
                else
                {

                }
            }

        }
        else
        {

            sender.sendMessage( OkiCore.getPfx() + "You don't have permission to get a starter kit! :O  Please notify an Admin! :3");

        }




        return false;



    }
}
*/
