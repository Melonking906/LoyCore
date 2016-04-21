package me.nonit.loycore.chat;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class ChannelStore {
    private HashMap<Player, Character> channels;
    private List<World> worlds = Bukkit.getWorlds();

    public ChannelStore() {
        this.channels = new HashMap<>();
    }

    public char getPlayerChannel(Player p) {
        if (channels.containsKey(p)) {
            return channels.get(p);
        } else {
            return 'g';
        }
    }

    public void setPlayerChannel(Player p, char channel) {
        channels.put(p, channel);
    }

    public World getRequestedWorld(Player p)
    {
        for(int x = 0; x < worlds.size(); x++) {

            if (worlds.get(x) == p.getWorld()) {

                return p.getWorld();

            }
            else
            {

                return null;

            }
        }

        return null;
    }

}

