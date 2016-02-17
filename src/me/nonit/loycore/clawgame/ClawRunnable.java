package me.nonit.loycore.clawgame;

import me.nonit.loycore.TitleMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClawRunnable extends BukkitRunnable
{
    private static final String BAR = "****************";
    private static final char INDICATOR = 'o';
    private int slot;
    private boolean up;
    
    public ClawRunnable()
    {
        this.slot = 0;
        this.up = true;
    }

    @Override
    public void run()
    {
        for ( Player player : Bukkit.getOnlinePlayers() )
        {
            String sendBar = BAR.substring( 0, slot ) + INDICATOR + BAR.substring( slot + 1 );

            TitleMessage.showMessage( player, ChatColor.BLUE + sendBar, ChatColor.YELLOW + "Win Loy -o- Loy Win", 3, 0, 0 );

            if ( up )
            {
                slot++;
            }
            else
            {
                slot--;
            }

            if ( slot >= 15 )
            {
                up = false;
            }
            if ( slot <= 0 )
            {
                up = true;
            }
        }
    }
}
