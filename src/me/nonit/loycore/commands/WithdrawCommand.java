package me.nonit.loycore.commands;

import me.nonit.loycore.LoyCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawCommand implements CommandExecutor
{
    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("Your a console....");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("loy.withdraw"))
        {
            p.sendMessage(LoyCore.getPfx() + ChatColor.RED + "You don't have permission for that!");
        }



        return true;
    }
}