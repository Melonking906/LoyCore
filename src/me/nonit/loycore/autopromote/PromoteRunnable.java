package me.nonit.loycore.autopromote;

import me.nonit.loycore.LoyCore;
import me.nonit.loycore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PromoteRunnable extends BukkitRunnable
{
    private final AutoPromote autoPromote;
    private final LoyCore plugin;
    private final String mollyPrefix;

    public PromoteRunnable( AutoPromote autoPromote, LoyCore plugin )
    {
        this.autoPromote = autoPromote;
        this.plugin = plugin;
        this.mollyPrefix = ChatColor.translateAlternateColorCodes( '&', plugin.getConfig().getString( "announce_prefix" ) ) + ChatColor.WHITE + " ";
    }

    @Override
    public void run()
    {
        HashMap<UUID,Date> waitinglist = autoPromote.getWaitinglist();

        for( Map.Entry<UUID,Date> waiter : waitinglist.entrySet() )
        {
            if( timeSince( waiter.getValue() ) >= 150 )
            {
                Player player = Bukkit.getPlayer( waiter.getKey() );

                if( player != null )
                {
                    if( ! LoyCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
                    {
                        LoyCore.permission.playerAddGroup( player, AutoPromote.PROMOTE_RANK );
                        autoPromote.removeUUID( waiter.getKey() );

                        TitleMessage.showMessage( player, ChatColor.GREEN + "\\o/ Congratz \\o/", ChatColor.YELLOW + "You've been promoted to builder!", 100 );

                        player.sendMessage( " " );
                        player.sendMessage( LoyCore.getPfx() + "\\o/ Congratz, you've been promoted to builder! \\o/" );
                        player.sendMessage( LoyCore.getPfx() + "Do /random to find empty land :D" );
                        player.sendMessage( " " );

                        plugin.db.setPromotedTime( player );

                        for( Player onlinePlayer : Bukkit.getServer().getOnlinePlayers() )
                        {
                            onlinePlayer.sendMessage( mollyPrefix + "Welcome! " + player.getDisplayName() + " joined for the first time!" );

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
}
