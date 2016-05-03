package com.okicraft.okicore.signs;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Created by skitt on 5/2/2016.
 */
public class SignColorzListener implements Listener {
    // CONSTRUCTOR
    public SignColorzListener() {
    }

    // EVENTS
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignPlace(SignChangeEvent e)
    {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        if(!(b.getState() instanceof Sign))
            return;

        if(!SignColorz.containsColorCodes(e.getLine(0) + " " + e.getLine(1) + " " + e.getLine(2) + " " + e.getLine(3)))
            return;

        if(!(p.hasPermission("sc.coloredsigns")))
        {
            p.sendMessage(SignColorz.permissions_message);
            return;
        }

        if(!(SignColorz.containsMagic(e.getLine(0) + " " + e.getLine(1) + " " + e.getLine(2) + " " + e.getLine(3))))
        {
            e.setLine(0, SignColorz.translateColorCodes(e.getLine(0)));
            e.setLine(1, SignColorz.translateColorCodes(e.getLine(1)));
            e.setLine(2, SignColorz.translateColorCodes(e.getLine(2)));
            e.setLine(3, SignColorz.translateColorCodes(e.getLine(3)));

            return;
        }

        if(p.hasPermission("sc.magic"))
        {
            e.setLine(0, SignColorz.translateColorCodes(e.getLine(0)));
            e.setLine(1, SignColorz.translateColorCodes(e.getLine(1)));
            e.setLine(2, SignColorz.translateColorCodes(e.getLine(2)));
            e.setLine(3, SignColorz.translateColorCodes(e.getLine(3)));
        }
    }





}
