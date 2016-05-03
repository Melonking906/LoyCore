package com.okicraft.okicore.autopromote;

import com.okicraft.okicore.OkiCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class AutoPromote
{
    private HashMap<UUID,Date> waitinglist;
    public static final String PROMOTE_RANK = "builder";

    public AutoPromote( OkiCore plugin )
    {
        this.waitinglist = new HashMap<>();

        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents( new PromoteListener( this ), plugin );

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask( plugin, new PromoteRunnable( this, plugin ), 200L, 200L ); // Runs every 10 seconds;
    }

    public boolean hasUUID( UUID uuid )
    {
        return waitinglist.containsKey( uuid );
    }

    public void addUUID( UUID uuid )
    {
        waitinglist.put( uuid, new Date() );
    }

    public void removeUUID( UUID uuid )
    {
        waitinglist.remove( uuid );
    }

    public HashMap<UUID, Date> getWaitinglist()
    {
        return waitinglist;
    }
}
