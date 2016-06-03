package com.okicraft.okicore;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Random;

public class PayRunnable extends BukkitRunnable
{
    private Random random;
    private Economy econ = OkiCore.gringottsEcon;

    private static final int MAX_RO = 100;
    private static final int MIN_RO = 500;

    public PayRunnable()
    {
        this.random = new Random();
    }

    @Override
    public void run()
    {
        int pocketMoney = random.nextInt(MAX_RO - MIN_RO) + MIN_RO;

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        for( Player player : players )
        {

            econ.depositPlayer( player, pocketMoney );
            TitleMessage.showMessage( player, "", ChatColor.GOLD + "You got " + ChatColor.YELLOW + pocketMoney + ChatColor.GOLD + " Ro in pocket money!", 80 );

        }
    }
}