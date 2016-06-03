package com.okicraft.okicore.autopromote;

import com.okicraft.okicore.OkiCore;
import com.okicraft.okicore.TitleMessage;
import org.bukkit.Bukkit;
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

        if( ! OkiCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
        {
            autoPromote.addUUID( player.getUniqueId() );

            //Send them to spawn on first join.
            player.teleport( Bukkit.getWorld( "Oki_1" ).getSpawnLocation() );

            player.sendMessage( " " );
            player.sendMessage( ChatColor.YELLOW + "Hello, new person! We are so glad you joined!" );
            player.sendMessage( ChatColor.GRAY + "Don't Worry! You'll be able to build in 1 min!" );
            player.sendMessage( ChatColor.GRAY + "While you wait, please enjoy exploring spawn!" );
            player.sendMessage( ChatColor.GRAY + "We know its all confusing now, so" );
            player.sendMessage( ChatColor.GRAY + "just ask in chat if you need any help ;3" );
            player.sendMessage( ChatColor.YELLOW + "Love - Oki Server Staff" );
            player.sendMessage( " " );

            String title = ChatColor.YELLOW + "Hi " + ChatColor.GREEN + player.getDisplayName() + ChatColor.YELLOW + "!";
            String subTitle = ChatColor.AQUA + "You're our new fav player <3";

            TitleMessage.showMessage( player, title, subTitle, 120 );

            OkiCore.staffBroadcast( player.getDisplayName() + " is new and has just joined!" );
        }
    }

    @EventHandler
    public void onQuit( PlayerQuitEvent evt )
    {
        Player player = evt.getPlayer();

        if( ! OkiCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
        {
            if( autoPromote.hasUUID( player.getUniqueId() ) )
            {
                autoPromote.removeUUID( player.getUniqueId() );
            }
        }
    }


}
