package me.nonit.loycore;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class InvSaverListener implements Listener
{
    private HashMap<UUID,Drop> invs;
    private LoyCore plugin;

    private static final double CLAIM_COST = 200;

    public InvSaverListener( LoyCore plugin )
    {
        invs = new HashMap<UUID, Drop>();
        this.plugin = plugin;
    }

    @EventHandler( priority = EventPriority.HIGH )
    public void onPlayerDeath( final PlayerDeathEvent e )
    {
        final Player p = e.getEntity().getPlayer();
        final Location l = p.getLocation();
        final World w = p.getWorld();

        final ItemStack[] armor = p.getInventory().getArmorContents();
        final ItemStack[] contents = p.getInventory().getContents();
        final float exp = p.getExp();

        e.getDrops().clear();

        if( invs.containsKey( p.getUniqueId() ) )
        {
            Drop oldDrop = invs.get( p.getUniqueId() );
            oldDrop.dropItems();
            invs.remove( p.getUniqueId() );
        }

        Drop drop = new Drop( p.getUniqueId(), contents, armor, exp, w, l );
        invs.put( p.getUniqueId(), drop );

        final Date dropDate = drop.getDate();

        p.sendMessage( LoyCore.getPfx() + "You died :O Block bandits saved your inventory for 1 min!" );
        //p.sendMessage( LoyCore.getPfx() + "Pay them " + ChatColor.YELLOW + LoyCore.economy.format( CLAIM_COST ) + ChatColor.GREEN + " with "+ ChatColor.YELLOW + "/invclaim" + ChatColor.GREEN + " or they will drop it!" );

        p.getServer().getScheduler().scheduleSyncDelayedTask( plugin, new Runnable()
        {
            @Override
            public void run()
            {
                if( invs.containsKey( p.getUniqueId() ) )
                {
                    Drop runDrop = invs.get( p.getUniqueId() );

                    if( runDrop.getDate().equals( dropDate ) )
                    {
                        invs.remove( p.getUniqueId() );
                        runDrop.dropItems();
                    }
                }
            }
        }, 1200L );
    }

    public Drop getInv( UUID uuid )
    {
        return invs.get( uuid );
    }

    public void removeInv( UUID uuid )
    {
        invs.remove( uuid );
    }

    public double getClaimCost()
    {
        return CLAIM_COST;
    }

    public class Drop
    {
        private final UUID uuid;
        private final Date date;
        private final ItemStack[] contents;
        private final ItemStack[] armor;
        private final float exp;
        private final World world;
        private final Location location;

        public Drop( UUID uuid, ItemStack[] contents, ItemStack[] armor, float exp, World world, Location location )
        {
            this.uuid = uuid;
            this.contents = contents;
            this.armor = armor;
            this.exp = exp;
            this.world = world;
            this.location = location;

            this.date = new Date();
        }

        public UUID getUuid()
        {
            return uuid;
        }

        public Date getDate()
        {
            return date;
        }

        public ItemStack[] getContents()
        {
            return contents;
        }

        public ItemStack[] getArmor()
        {
            return armor;
        }

        public float getExp()
        {
            return exp;
        }

        public World getWorld()
        {
            return world;
        }

        public Location getLocation()
        {
            return location;
        }

        public void dropItems()
        {
            for( ItemStack contentsItem : contents )
            {
                if( contentsItem != null && ! contentsItem.getType().equals( Material.AIR ) )
                {
                    world.dropItem( location, contentsItem );
                }
            }
            for( ItemStack armorItem : armor )
            {
                if( armorItem != null && ! armorItem.getType().equals( Material.AIR ) )
                {
                    world.dropItem( location, armorItem );
                }
            }
        }
    }
}
