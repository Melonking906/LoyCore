package me.nonit.loycore;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GameModesListener implements Listener
{
    public GameModesListener()
    {

    }

    @EventHandler
    public void onWorldChange( PlayerChangedWorldEvent event )
    {
        Player player = event.getPlayer();
        World toWorld = player.getWorld();

        if( toWorld.getName().equals( "Space" ) )
        {
            if( !player.hasPermission( "loy.access.space" ) )
            {
                player.sendMessage( LoyCore.getPfx() + ChatColor.RED + "You need space air from spawn first!" );

                player.teleport( event.getFrom().getSpawnLocation() );
                return;
            }

            player.addPotionEffect(new PotionEffect( PotionEffectType.JUMP, 99999, 3 ));
        }
        else
        {
            player.addPotionEffect(new PotionEffect( PotionEffectType.JUMP, 0, 0 ), true);
        }

        if( player.hasPermission( "loy.gmanyworld" ) )
        {
            return;
        }

        if( toWorld.getName().equals( "Lake" ) )
        {
            player.setGameMode( GameMode.CREATIVE );
        }
        else
        {
            player.setGameMode( GameMode.SURVIVAL );

            if( player.hasPermission( "loy.fly" ) )
            {
                player.setAllowFlight( true );
                player.setFlying( true );
            }
            else
            {
                player.setAllowFlight( false );
                player.setFlying( false );
            }
        }
    }

    @EventHandler
    public void onBlockPlace( BlockPlaceEvent event )
    {
        if( event == null )
        {
            return;
        }

        World world = event.getBlock().getWorld();

        if( ! world.getName().contains( "Mine" ) )
        {
            return;
        }

        Player player = event.getPlayer();

        if( player == null )
        {
            return;
        }

        ActionMessage.showMessage( player, ChatColor.RED + "Remember this world gets reset!" );
    }
}
