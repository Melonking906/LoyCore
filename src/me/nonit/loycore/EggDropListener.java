package me.nonit.loycore;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Random;

public class EggDropListener implements Listener
{
    private final HashSet<EntityType> types;
    private final Random random;

    public EggDropListener()
    {
        types = new HashSet<>();
        types.add( EntityType.CREEPER );
        types.add( EntityType.SKELETON );
        types.add( EntityType.SPIDER );
        types.add( EntityType.ZOMBIE );
        types.add( EntityType.CAVE_SPIDER );
        types.add( EntityType.PIG );
        types.add( EntityType.SHEEP );
        types.add( EntityType.COW );
        types.add( EntityType.CHICKEN );
        types.add( EntityType.SQUID );
        types.add( EntityType.WOLF );
        types.add( EntityType.MUSHROOM_COW );
        types.add( EntityType.OCELOT );
        types.add( EntityType.HORSE );
        types.add( EntityType.RABBIT );
        types.add( EntityType.VILLAGER );
        types.add( EntityType.BAT );
        types.add( EntityType.BLAZE );
        types.add( EntityType.WITCH );

        random = new Random();
    }

    @EventHandler
    public void onDeath( EntityDeathEvent e )
    {
        if( e == null )
        {
            return;
        }

        Entity entity = e.getEntity();
        Player k = e.getEntity().getKiller();

        if( k == null )
        {
            return;
        }

        //Fail Chance
        int chance = 0;
        Material weaponType = k.getItemInHand().getType();

        switch( weaponType )
        {
            case BOW: chance = 100;
                break;
            case WOOD_SWORD: chance = 10;
                break;
            case STONE_SWORD: chance = 20;
                break;
            case IRON_SWORD: chance = 30;
                break;
            case GOLD_SWORD: chance = 100;
                break;
            case DIAMOND_SWORD: chance = 40;
                break;
            default: break;
        }

        if( chance <= random.nextInt(99) )
        {
            return;
        }

        short id = -1;

        for ( EntityType type : types )
        {
            if( entity.getType().equals( type ) )
            {
                id = type.getTypeId();
            }
        }

        if( id == -1 )
        {
            return;
        }

        ItemStack egg = new ItemStack( Material.MONSTER_EGG, 1, id );

        entity.getWorld().dropItem( entity.getLocation(), egg );
    }

    // Disable Eggs on Spawners
    @EventHandler( priority = EventPriority.HIGHEST )
    public void onClick( PlayerInteractEvent e )
    {
        if( ! ( e.getAction().equals( Action.RIGHT_CLICK_BLOCK ) || e.getAction().equals( Action.LEFT_CLICK_BLOCK ) ) )
        {
            return;
        }

        if( ! e.getClickedBlock().getType().equals( Material.MOB_SPAWNER ) )
        {
            return;
        }

        if( ! e.getPlayer().getItemInHand().getType().equals( Material.MONSTER_EGG ) )
        {
            return;
        }

        if( ! e.getPlayer().hasPermission( "loy.eggonspawner" ) )
        {
            e.setCancelled( true );
        }
    }
}