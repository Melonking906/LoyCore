package com.okicraft.okicore;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GameModesListener implements Listener {
    public GameModesListener() {

    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World toWorld = player.getWorld();

        if (player.hasPermission("oki.gmanyworld")) {
            return;
        }

        if(toWorld.getName().equals("Spore")) {
            player.setGameMode(GameMode.SURVIVAL);
            if (player.hasPermission("oki.fly")) {

                player.setAllowFlight(true);
                player.setFlying(true);
            }
            else
            {

                OkiCore.playErrorSound( player );
                player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Oops!  Looks like you don't have permission to fly in the Island World!");
                player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "Please let an " + ChatColor.GRAY + "<" + ChatColor.BLUE + "Admin" + ChatColor.GRAY + ">" + ChatColor.RED + "know!");

            }
        } else {
            if(player.hasPermission( "oki.fly.anyworld" ) )
            {

               player.setAllowFlight( true);
                player.setFlying( true );

            }
            else {

                OkiCore.playErrorSound(player);
                player.sendMessage(OkiCore.getPfx() + ChatColor.RED + "You don't have permission to fly in this world!");

            }

        }
    }
/*
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
    } */
}