package com.okicraft.okicore;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Random;

import static com.okicraft.okicore.OkiCore.permission;

public class PayRunnable extends BukkitRunnable
{
    private Random random;
    private Economy econ = OkiCore.gringottsEcon;

    private static final int MAX_RO_DEFAULT = 150;
    private static final int MIN_RO_DEFAULT = 15;

    private static final int MAX_RO_RAVEN = (int) (MAX_RO_DEFAULT * 1.15);
    private static final int MIN_RO_RAVEN = (int) (MIN_RO_DEFAULT * 1.15);

    private static final int MAX_RO_FALCON = (int) (MAX_RO_DEFAULT * 1.30);
    private static final int MIN_RO_FALCON = (int) (MIN_RO_DEFAULT * 1.30);

    private static final int MAX_RO_PHOENIX = (int) (MAX_RO_DEFAULT * 1.45);
    private static final int MIN_RO_PHOENIX = (int) (MIN_RO_DEFAULT * 1.45);

    public PayRunnable()
    {
        this.random = new Random();
    }

    @Override
    public void run()
    {
        int pocketMoney = 0;
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        for( Player player : players )
        {
            if(permission.playerInGroup( player, "Raven" ) )
            {

                pocketMoney = random.nextInt(MAX_RO_RAVEN - MIN_RO_RAVEN) + MIN_RO_RAVEN;
                econ.depositPlayer( player, pocketMoney );
                TitleMessage.showMessage( player, "", ChatColor.GOLD + "You got " + ChatColor.YELLOW + pocketMoney + ChatColor.GOLD + " Ro in pocket money!", 80 );
                player.sendMessage( OkiCore.getPfx() + ChatColor.GREEN + "Thank you, " + player.getDisplayName() + ChatColor.GREEN + ", for your support! :D" );
                player.sendMessage( OkiCore.getPfx() + ChatColor.GOLD + "You got " + ChatColor.YELLOW + pocketMoney + ChatColor.GOLD + " Ro in pocket money!" );
            }
            else if( permission.playerInGroup( player, "Falcon" ) )
            {

                pocketMoney = random.nextInt(MAX_RO_FALCON - MIN_RO_FALCON) + MIN_RO_FALCON;
                econ.depositPlayer( player, pocketMoney );
                TitleMessage.showMessage( player, "", ChatColor.GOLD + "You got " + ChatColor.YELLOW + pocketMoney + ChatColor.GOLD + " Ro in pocket money!", 80 );
                player.sendMessage( OkiCore.getPfx() + ChatColor.GREEN + "Thank you, " + player.getDisplayName() + ChatColor.GREEN + ", for your support! :D" );
                player.sendMessage( OkiCore.getPfx() + ChatColor.GOLD + "You got " + ChatColor.YELLOW + pocketMoney + ChatColor.GOLD + " Ro in pocket money!" );
            }
            else if( permission.playerInGroup( player, "Phoenix" ) )
            {

                pocketMoney = random.nextInt(MAX_RO_PHOENIX - MIN_RO_PHOENIX) + MIN_RO_PHOENIX;
                econ.depositPlayer( player, pocketMoney );
                TitleMessage.showMessage( player, "", ChatColor.GOLD + "You got " + ChatColor.YELLOW + pocketMoney + ChatColor.GOLD + " Ro in pocket money!", 80 );
                player.sendMessage( OkiCore.getPfx() + ChatColor.GREEN + "Thank you, " + player.getDisplayName() + ChatColor.GREEN + ", for your support! :D" );
                player.sendMessage( OkiCore.getPfx() + ChatColor.GOLD + "You got " + ChatColor.YELLOW + pocketMoney + ChatColor.GOLD + " Ro in pocket money!" );
            }
            else
            {

                pocketMoney = random.nextInt(MAX_RO_DEFAULT - MIN_RO_DEFAULT) + MIN_RO_DEFAULT;
                econ.depositPlayer( player, pocketMoney );
                TitleMessage.showMessage( player, "", ChatColor.GOLD + "You got " + ChatColor.YELLOW + pocketMoney + ChatColor.GOLD + " Ro in pocket money!", 80 );
                player.sendMessage( OkiCore.getPfx() + ChatColor.GOLD + "You got " + ChatColor.YELLOW + pocketMoney + ChatColor.GOLD + " Ro in pocket money!" );
            }
        }
    }
}