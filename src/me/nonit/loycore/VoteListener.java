package me.nonit.loycore;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VoteListener implements Listener
{
    private int votes;
    private Random random;

    private List<Material> rewards;

    private static final int VOTES_PER_REWARD = 6;

    public VoteListener()
    {
        votes = 0;
        random = new Random();

        rewards = new ArrayList<>();
        rewards.add( Material.DIAMOND );
        rewards.add( Material.BLAZE_ROD );
        rewards.add( Material.CAKE );
        rewards.add( Material.CHAINMAIL_BOOTS );
        rewards.add( Material.CHAINMAIL_CHESTPLATE );
        rewards.add( Material.CHAINMAIL_HELMET );
        rewards.add( Material.CHAINMAIL_LEGGINGS );
        rewards.add( Material.COCOA );
        rewards.add( Material.CARROT );
        rewards.add( Material.EMERALD );
        rewards.add( Material.FLOWER_POT_ITEM );
        rewards.add( Material.EXP_BOTTLE );
        rewards.add( Material.EGG );
        rewards.add( Material.POTATO_ITEM );
        rewards.add( Material.SPONGE );
        rewards.add( Material.GRASS );
        rewards.add( Material.WORKBENCH );
        rewards.add( Material.DIRT );
        rewards.add( Material.BOWL );
        rewards.add( Material.FISHING_ROD );
        rewards.add( Material.PUMPKIN_PIE );
        rewards.add( Material.IRON_BLOCK);
        rewards.add( Material.GOLD_BLOCK );
        rewards.add( Material.EYE_OF_ENDER );
        rewards.add( Material.WATCH );
        rewards.add( Material.COMPASS );
        rewards.add( Material.ARMOR_STAND );
        rewards.add( Material.BUCKET );
    }

    @EventHandler( priority = EventPriority.NORMAL )
    public void onVotifierEvent( VotifierEvent event )
    {
        Vote vote = event.getVote();
        Player p = Bukkit.getServer().getPlayer( vote.getUsername() );

        votes++;

        if( p != null )
        {
            Bukkit.getServer().broadcastMessage( LoyCore.getMol() + "Yay! " + p.getDisplayName() + " voted for LoyLoy, just " + ChatColor.YELLOW + (VOTES_PER_REWARD - votes) + ChatColor.WHITE + " more until we have a party :D" );

            //Give the p an I voted cookie.
            PlayerInventory inventory = p.getInventory();

            String myDisplayName = "I Voted!";

            ItemStack cookie = new ItemStack( Material.COOKIE, 1 );
            ItemMeta im = cookie.getItemMeta();
            im.setDisplayName( myDisplayName );
            cookie.setItemMeta( im );

            inventory.addItem( cookie );

            //Give a vote crate
            Bukkit.dispatchCommand( Bukkit.getConsoleSender(), "crate give " + p.getName() + " vote 1" );
        }
        else
        {
            Bukkit.getServer().broadcastMessage( LoyCore.getMol() + "Yay! " + vote.getUsername() + " voted for LoyLoy, just " + ChatColor.YELLOW + (VOTES_PER_REWARD - votes) + ChatColor.WHITE + " more until we have a party :D" );
        }

        if( votes == VOTES_PER_REWARD )
        {
            String title = ChatColor.YELLOW + "Vote Party!" + ChatColor.GRAY + " Well done everyone!";

            for( Player player : Bukkit.getServer().getOnlinePlayers() )
            {
                int pick = random.nextInt( rewards.size() );
                int amount = random.nextInt( 8 ) + 3;

                ItemStack reward = new ItemStack( rewards.get( pick ), amount );
                player.getInventory().addItem( reward );

                String subTitle = ChatColor.GREEN + "You got " + amount + " " + prettyName( reward.getType().name() ) + "'s :D";
                player.sendMessage( LoyCore.getPfx() + subTitle );

                TitleMessage.showMessage( player, title, subTitle, 90 );
            }

            votes = 0;
        }
    }

    private static String prettyName( String rawName )
    {
        String name = rawName.replace( '_', ' ' );
        name = WordUtils.capitalizeFully( name );
        name = name.replaceAll( "item", "" );
        return name;
    }
}
