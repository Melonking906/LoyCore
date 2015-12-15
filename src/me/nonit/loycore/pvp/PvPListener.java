package me.nonit.loycore.pvp;

import me.nonit.loycore.ActionMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class PvPListener implements Listener
{
    private List<Player> pvpPlayers;

    public PvPListener()
    {
        this.pvpPlayers = new ArrayList<>();
    }

    @EventHandler
    public void onPvP( EntityDamageByEntityEvent event )
    {
        if( event.isCancelled() )
        {
            return;
        }

        if( event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK )
        {
            return;
        }

        if( !(event.getEntity() instanceof Player) )
        {
            return;
        }

        Player victim = (Player)event.getEntity();
        Player damager;

        if( event.getDamager() instanceof Player )
        {
            damager = (Player) event.getDamager();
        }
        else if( event.getDamager() instanceof Projectile )
        {
            Projectile projectile = (Projectile) event.getDamager();

            if( !(projectile.getShooter() instanceof Player) )
            {
                return;
            }

            damager = (Player) projectile.getShooter();
        }
        else
        {
            return;
        }

        boolean blockPvP = false;
        if( !pvpPlayers.contains( victim ) )
        {
            blockPvP = true;
        }
        if( !pvpPlayers.contains( damager ) )
        {
            blockPvP = true;
        }

        if( blockPvP )
        {
            event.setCancelled( true );

            String message = ChatColor.RED + "You both gotta to do /pvp!";
            ActionMessage.showMessage( damager, message );
            ActionMessage.showMessage( victim, message );
        }
    }

    public boolean addPvPPlayer(Player player)
    {
        return pvpPlayers.add( player );
    }

    public boolean removePvPPlayer(Player player)
    {
        return pvpPlayers.remove( player );
    }

    public boolean containsPvPPlayer(Player player)
    {
        return pvpPlayers.contains( player );
    }
}
