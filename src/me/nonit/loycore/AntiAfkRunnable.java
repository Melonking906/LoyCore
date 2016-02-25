package me.nonit.loycore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class AntiAfkRunnable extends BukkitRunnable
{
    private HashMap<UUID,Location> locations;

    public AntiAfkRunnable()
    {
        this.locations = new HashMap<>();
    }

    @Override
    public void run()
    {
        HashMap<UUID, Location> newLocations = new HashMap<>();

        //Check players to see if they are afk or not logged
        for( Player onlinePlayer : Bukkit.getOnlinePlayers() )
        {
            if ( locations.containsKey( onlinePlayer.getUniqueId() ) )
            {
                boolean kick = false;

                Location savedLocation = locations.get( onlinePlayer.getUniqueId() );
                Location playerLocation = onlinePlayer.getLocation();

                if ( playerLocation.getBlockX() == savedLocation.getBlockX() )
                {
                    if ( playerLocation.getBlockY() == savedLocation.getBlockY() )
                    {
                        if ( playerLocation.getBlockZ() == savedLocation.getBlockZ() )
                        {
                            kick = true;
                        }
                    }
                }

                if ( kick )
                {
                    onlinePlayer.kickPlayer( "You were afk too long!" );
                }
                else
                {
                    newLocations.put( onlinePlayer.getUniqueId(), onlinePlayer.getLocation() );
                }
            }
            else
            {
                newLocations.put( onlinePlayer.getUniqueId(), onlinePlayer.getLocation() );
            }
        }

        locations = newLocations;
    }
}