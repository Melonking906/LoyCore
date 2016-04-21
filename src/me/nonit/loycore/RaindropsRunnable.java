package me.nonit.loycore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RaindropsRunnable extends BukkitRunnable
{
    private List<Material> drops;
    private List<Biome> dryBiomes;
    private Random random;

    public RaindropsRunnable()
    {
        this.drops = new ArrayList<>();
        this.dryBiomes = new ArrayList<>();
        this.random = new Random();

        dryBiomes.add( Biome.BEACHES );
        dryBiomes.add( Biome.DESERT );
        dryBiomes.add( Biome.DESERT_HILLS );
        dryBiomes.add( Biome.MUTATED_DESERT);
        dryBiomes.add( Biome.SAVANNA );
        dryBiomes.add( Biome.SAVANNA_ROCK );
        dryBiomes.add( Biome.MUTATED_SAVANNA );
        dryBiomes.add( Biome.MUTATED_SAVANNA_ROCK );

        drops.add( Material.WHEAT );
        drops.add( Material.DIRT );
        drops.add( Material.GRASS );
        drops.add( Material.SLIME_BALL );
        drops.add( Material.APPLE );
        drops.add( Material.EMERALD );
        drops.add( Material.MOSSY_COBBLESTONE );
        drops.add( Material.EXP_BOTTLE );
        drops.add( Material.SEEDS );
        drops.add( Material.COOKIE );
    }

    @Override
    public void run()
    {
        for ( Player player : Bukkit.getOnlinePlayers() )
        {
            World world = player.getWorld();

            if ( world.hasStorm() )
            {
                if ( random.nextInt( 100 ) > 25 )
                {
                    return;
                }

                Location location = player.getLocation();

                if ( dryBiomes.contains( location.getBlock().getBiome() ) )
                {
                    return;
                }

                if ( world.getHighestBlockAt( location ).getY() > location.getY() )
                {
                    return;
                }

                int pick = random.nextInt( drops.size() );
                ItemStack drop = new ItemStack( drops.get( pick ) );

                location.setX( location.getX() + random.nextInt( 10 ) );
                location.setX( location.getX() - random.nextInt( 10 ) );
                location.setZ( location.getZ() + random.nextInt( 10 ) );
                location.setZ( location.getZ() - random.nextInt( 10 ) );
                location.setY( location.getY() + 20 );

                world.dropItem( location, drop );
            }
        }
    }
}
