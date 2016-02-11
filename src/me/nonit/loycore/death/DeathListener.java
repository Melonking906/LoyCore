package me.nonit.loycore.death;

import me.nonit.loycore.LoyCore;
import me.nonit.loycore.database.SQL;
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
import org.bukkit.event.player.PlayerQuitEvent;

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
                event.getPlayer().kickPlayer(ChatColor.RED + "You're dead until - " + niceDateFormat.format(deadPlayer.getDateOfAwake()));
                return;
            }
        }
    }

    @EventHandler( priority = EventPriority.HIGHEST )
    public void onLeave( PlayerQuitEvent event )
    {
        for( SQL.DeadPlayer deadPlayer : death.getDeadPlayers() )
        {
            if( deadPlayer.getUuid().equals( event.getPlayer().getUniqueId() ) )
            {
                return;
            }
        }
    }

    @EventHandler( priority = EventPriority.HIGH )
    public void onEntityDamage( EntityDamageEvent event )
    {
        Entity entity = event.getEntity();
        if( !entity.getType().equals( EntityType.PLAYER ) )
        {
            return;
        }

        Player player = (Player)entity;

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

        Location spawn = Bukkit.getWorlds().get( 0 ).getSpawnLocation();
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
        Bukkit.broadcastMessage( LoyCore.getPfx() + ChatColor.GRAY + "You feel a cold shiver... " + player.getDisplayName() + " has died.");

        dieingPlayers.remove( player );
    }
}