package com.okicraft.okicore;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.projectiles.ProjectileSource;

public class DontBuildListener implements Listener
{
    private final static String PERMISSION = "oki.build";

    public DontBuildListener() {}

    @EventHandler( priority = EventPriority.HIGH )
    public void onPlayerChat( AsyncPlayerChatEvent event )
    {
        if( event.isCancelled() ) { return; }

        Player player = event.getPlayer();
        if( hasNoPermission( player ) )
        {
            event.setCancelled( true );

            TitleMessage.showMessage( player, "", ChatColor.RED + "You can't chat yet, please wait 1 min from join!", 60 );
        }
    }

    @EventHandler( priority = EventPriority.HIGH )
    public void onBlockBreak( BlockBreakEvent event )
    {
        if( event.isCancelled() ) { return; }

        Player player = event.getPlayer();
        if( hasNoPermission( player ) )
        {
            event.setCancelled( true );

            TitleMessage.showMessage( player, "", ChatColor.RED + "Please wait for your rank to load! (1 min from join)", 60 );
        }
    }

    @EventHandler( priority = EventPriority.HIGH )
    public void onBlockPlace( BlockPlaceEvent event )
    {
        if( event.isCancelled() ) { return; }

        Player player = event.getPlayer();
        if( hasNoPermission( player ) )
        {
            event.setCancelled( true );
            TitleMessage.showMessage( player, "", ChatColor.RED + "Please wait for your rank to load! (1 min from join)", 60 );
        }
    }

    @EventHandler( priority = EventPriority.HIGH )
    public void onHangingBreak( HangingBreakByEntityEvent event )
    {
        if( event.isCancelled() ) { return; }

        if( hasNoPermission( event.getEntity() ) )
        {
            event.setCancelled( true );
            Player player = (Player) event.getEntity();
            TitleMessage.showMessage( player, "", ChatColor.RED + "Please wait for your rank to load! (1 min from join)", 60 );
        }
    }

    @EventHandler( priority = EventPriority.HIGH )
    public void onPlayerDamage( EntityDamageByEntityEvent event )
    {
        if( event.isCancelled() ) { return; }

        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        if( entity instanceof Animals )
        {
            if( hasNoPermission( entity ) )
            {
                Player player = (Player) entity;
                event.setCancelled( true );
                TitleMessage.showMessage( player, "", ChatColor.RED + "Please wait for your rank to load! (1 min from join)", 60 );
            }
            else if( damager instanceof Projectile )
            {
                ProjectileSource source = ( ( Projectile ) damager ).getShooter();

                if( ! (source instanceof Entity) )
                {
                    return;
                }

                Entity shooter = (Entity) source;

                if( ! (shooter instanceof Player) )
                {
                    return;
                }

                Player player = (Player) shooter;
                if( ! player.hasPermission( PERMISSION ) )
                {
                    event.setCancelled( true );
                }
            }
        }

        if( entity instanceof ItemFrame )
        {
            if( ! (damager instanceof Player) )
            {
                return;
            }
            Player player = (Player) damager;

            if( hasNoPermission( player ) )
            {
                event.setCancelled( true );
                TitleMessage.showMessage( player, "", ChatColor.RED + "Please wait for your rank to load! (1 min from join)", 60 );
            }
        }
    }

    private boolean hasNoPermission( Entity entity )
    {
        if( ! (entity instanceof Player) )
        {
            return false;
        }

        Player player = (Player) entity;
        return ! player.hasPermission( PERMISSION );
    }
}