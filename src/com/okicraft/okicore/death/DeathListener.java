package com.okicraft.okicore.death;

import com.okicraft.okicore.OkiCore;
import com.okicraft.okicore.database.SQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DeathListener implements Listener
{
    private Death death;
    private SimpleDateFormat niceDateFormat;
    private List<Player> dieingPlayers;

    public DeathListener( Death death )
    {
        this.niceDateFormat = new SimpleDateFormat( "h:mm a" );
        this.death = death;
        this.dieingPlayers = new ArrayList<>();
    }

    @EventHandler( priority = EventPriority.HIGHEST )
    public void onJoin( PlayerJoinEvent event )
    {
        for( SQL.DeadPlayer deadPlayer : death.getDeadPlayers() )
        {
            if( deadPlayer.getUuid().equals(event.getPlayer().getUniqueId()) )
            {
                event.setJoinMessage( null );
                event.getPlayer().kickPlayer(ChatColor.RED + "You're dead for 5 mins! - Till.. " + niceDateFormat.format(deadPlayer.getDateOfAwake()) + " New York time!");
                return;
            }
        }
    }

    @EventHandler( priority = EventPriority.LOW )
    public void onEntityDamage( EntityDamageEvent event )
    {
        if(event.isCancelled())
        {
            return;
        }

        Entity entity = event.getEntity();
        if( !entity.getType().equals( EntityType.PLAYER ) )
        {
            return;
        }

        Player player = (Player)entity;

        //Disable unwanted damages
        if ( event.getCause() == EntityDamageEvent.DamageCause.FALL
                || event.getCause() == EntityDamageEvent.DamageCause.VOID
                || event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION
                || event.getCause() == EntityDamageEvent.DamageCause.STARVATION
                || event.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK )
        {
            event.setCancelled( true );
            return;
        }

        if( (player.getHealth() - event.getDamage()) > 0 )
        {
            return;
        }

        //Dont let dead players get any more damage.
        if( dieingPlayers.contains( player ) )
        {
            event.setCancelled( true );
            return;
        }

        //It is a player death, so handel death mechanics.

        event.setCancelled( true );

        dieingPlayers.add( player );

        Location spawn = Bukkit.getWorld( "Space" ).getSpawnLocation();
        if( player.getBedSpawnLocation() != null )
        {
            spawn = player.getBedSpawnLocation();
        }

        player.playEffect( EntityEffect.DEATH );
        player.teleport( spawn );
        player.setHealth( player.getMaxHealth() );
        player.setFoodLevel( 20 );
        player.setExp( 0 );
        player.setLevel( 0 );

        death.getDB().updatePlayerDeath(player);
        death.refreshDeadPlayers();

        player.kickPlayer(ChatColor.RED + "You died...");
        Bukkit.broadcastMessage( OkiCore.getPfx() + ChatColor.GRAY + "You feel a cold shiver... " + player.getDisplayName() + " has died.");

        dieingPlayers.remove( player );
    }
}