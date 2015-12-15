package me.nonit.loycore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Random;

public class PayRunnable extends BukkitRunnable
{
    private LoyCore plugin;
    private Random random;

    private static final int MAX_FE = 80;
    private static final int MIN_FE = 20;

    private static final double MAX_PLAYER_FE_FOR_PAY = 2000;

    public PayRunnable( LoyCore plugin )
    {
        this.plugin = plugin;
        this.random = new Random();
    }

    @Override
    public void run()
    {
        int pocketMoney = random.nextInt(MAX_FE - MIN_FE) + MIN_FE;

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        for( Player player : players )
        {
            if( LoyCore.economy.getBalance( player ) < MAX_PLAYER_FE_FOR_PAY )
            {
                LoyCore.economy.depositPlayer( player, pocketMoney );

                player.sendMessage(LoyCore.getPfx() + "You got " + ChatColor.YELLOW + LoyCore.economy.format(pocketMoney) + ChatColor.GREEN + " pocket money!");

                TitleMessage.showMessage( player, "", ChatColor.GOLD + "You got " + ChatColor.YELLOW + LoyCore.economy.format( pocketMoney ) + ChatColor.GOLD + " pocket money!", 80 );
            }
        }
    }
}