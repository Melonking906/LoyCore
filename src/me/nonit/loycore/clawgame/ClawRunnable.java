package me.nonit.loycore.clawgame;

import me.nonit.loycore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClawRunnable extends BukkitRunnable
{
    public ClawRunnable() {}

    @Override
    public void run()
    {
        for ( Player player : Bukkit.getOnlinePlayers() )
        {
            TitleMessage.showMessage( player, System.currentTimeMillis() + "", "Loy", 1, 0, 0 );
        }
    }
}
