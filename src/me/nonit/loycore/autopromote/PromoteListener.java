package me.nonit.loycore.autopromote;

import me.nonit.loycore.LoyCore;
import me.nonit.loycore.TitleMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PromoteListener implements Listener
{
    private AutoPromote autoPromote;

    public PromoteListener( AutoPromote autoPromote )
    {
        this.autoPromote = autoPromote;
    }

    @EventHandler
    public void onJoin( PlayerJoinEvent evt )
    {
        final Player player = evt.getPlayer();

        if( ! LoyCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
        {
            autoPromote.addUUID( player.getUniqueId() );

            if( player.isOnline() )
            {
                player.sendMessage( " " );
                player.sendMessage( LoyCore.getPfx() + "Welcome newbie, please wait 1 minute online" );
                player.sendMessage( LoyCore.getPfx() + "to build and chat, we like to keep things spam free!" );
                player.sendMessage( " " );

                String title = ChatColor.GRAY + "Hail " + ChatColor.GREEN + player.getDisplayName() + ChatColor.GRAY + "!";
                String subTitle = ChatColor.YELLOW + "You will be promoted to builder in 1 minute!";

                TitleMessage.showMessage( player, title, subTitle, 80 );
            }
        }
    }

    @EventHandler
    public void onQuit( PlayerQuitEvent evt )
    {
        Player player = evt.getPlayer();

        if( ! LoyCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
        {
            if( autoPromote.hasUUID( player.getUniqueId() ) )
            {
                autoPromote.removeUUID( player.getUniqueId() );
            }
        }
    }
}
