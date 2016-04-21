package me.nonit.loycore.chat;

import me.nonit.loycore.LoyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class IgnoreCommand implements CommandExecutor {


    public IgnoreCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Sorry, you can't ignore someone from console!");

        } else {

            Player p = (Player) sender;

            if (args.length < 1) {

                sender.sendMessage(LoyCore.getPfx() + ChatColor.RED + "You're missing a part of the command. Try " + ChatColor.GREEN + "/ignore <player>");

            } else if (!(p.hasPermission("loy.chat.ignore"))) {

                sender.sendMessage(LoyCore.getPfx() + ChatColor.RED + "Hmm... it looks like you don't have permission for that!");
                sender.sendMessage(LoyCore.getPfx() + ChatColor.RED + "Please tell an Admin or an Owner about this!  Thank you! ^-^");

            } else if (args.length == 1) {
                if (args[0] == "list") {

                }


                System.out.print(" Added '" + args[0] + "' to the ignored list for player '" + p.getName() + "'.  "); // Printing to console.
            }
        }

        return false;
    }


    public boolean isIgnored( Player p ) {


        return false;

    }
}
