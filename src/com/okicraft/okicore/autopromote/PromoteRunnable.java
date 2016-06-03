package com.okicraft.okicore.autopromote;

import com.okicraft.okicore.OkiCore;
import com.okicraft.okicore.TitleMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PromoteRunnable extends BukkitRunnable
{
    private final AutoPromote autoPromote;
    private final OkiCore plugin;
    private final Economy econ;
    private final double start_amt = 1000.0;
    public PromoteRunnable( AutoPromote autoPromote, OkiCore plugin, Economy econ )
    {
        this.autoPromote = autoPromote;
        this.plugin = plugin;
        this.econ = econ;
    }

    @Override
    public void run()
    {
        HashMap<UUID,Date> waitinglist = autoPromote.getWaitinglist();

        if ( waitinglist.isEmpty() )
        {
            return;
        }

        for( Map.Entry<UUID,Date> waiter : waitinglist.entrySet() )
        {
            if( timeSince( waiter.getValue() ) >= 60 )
            {
                Player player = Bukkit.getPlayer( waiter.getKey() );

                if( player != null )
                {
                    if( ! OkiCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
                    {
                        OkiCore.permission.playerAddGroup( player, AutoPromote.PROMOTE_RANK );
                        autoPromote.removeUUID( waiter.getKey() );

                        TitleMessage.showMessage( player, ChatColor.GREEN + "\\o/ Congratz \\o/", ChatColor.YELLOW + "You've been promoted to builder!", 100 );

                        player.sendMessage( " " );
                        player.sendMessage( OkiCore.getPfx() + "\\o/ Congratz, you've been promoted to builder! \\o/" );
                        player.sendMessage( OkiCore.getPfx() + "Say hello to everyone!" );
                        player.sendMessage( " " );

                        plugin.db.setPromotedTime( player );

                        for( Player onlinePlayer : Bukkit.getServer().getOnlinePlayers() )
                        {
                            onlinePlayer.sendMessage( OkiCore.getMol() + "Yass! " + player.getDisplayName() + " is now a member!" );

                            if( ! onlinePlayer.equals( player ) )
                            {
                                TitleMessage.showMessage( onlinePlayer, "", ChatColor.YELLOW + player.getDisplayName() + ChatColor.GREEN + " has been promoted, welcome them!", 80 );
                            }
                        }
                    }
                }
            }
        }
    }

    private long timeSince( Date since )
    {
        Date currentDate = new Date();
        return (currentDate.getTime()-since.getTime())/1000;
    }

    private void setupNewAccount( Player p )
    {

        econ.createPlayerAccount( (OfflinePlayer) p );
        econ.depositPlayer( (OfflinePlayer) p, start_amt );

    }
}
