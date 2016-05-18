package com.okicraft.okicore.prefix;

import com.okicraft.okicore.OkiCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrefixListener implements Listener
{

    private Economy econ = OkiCore.gringottsEcon;
    private Random random;
    private List<ChatColor> colors;

    public PrefixListener()
    {
        random = new Random();

        colors = new ArrayList<>();
        colors.add( ChatColor.AQUA );
        colors.add( ChatColor.DARK_GRAY );
        colors.add( ChatColor.RED );
        colors.add( ChatColor.BLUE );
        colors.add( ChatColor.GREEN );
        colors.add( ChatColor.LIGHT_PURPLE );
    }

    @EventHandler
    public void onSignChange( SignChangeEvent event )
    {
        if( event.isCancelled() )
        {
            return;
        }

        if( event.getPlayer() == null )
        {
            return;
        }

        Player player = event.getPlayer();

        if( ChatColor.stripColor( event.getLine( 0 ) ).toLowerCase().equals( "[prefix]" ) )
        {
            String priceString = ChatColor.stripColor( event.getLine( 3 ) );
            String prefix;

            String blank1 = ChatColor.stripColor( event.getLine( 1 ) );
            String blank2 = ChatColor.stripColor( event.getLine( 2 ) );

            if( blank1 == null || blank1.equals( "" ) || blank1.equals( " " ) )
            {
                prefix = blank2;
            }
            else
            {
                prefix = blank1;
            }

            boolean error = false;

            if( !player.hasPermission( "oki.makeprefix" ) )
            {
                player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "You don't have permission to make prefixes!" );
                error = true;

            }

            if( prefix == null || prefix.equals( "" ) || prefix.equals( " " ) )
            {
                player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "You must include a prefix!" );
                error = true;
            }

            int price;
            try
            {
                price = Integer.parseInt( priceString.split( " " )[0] );
            }
            catch( Exception e )
            {
                price = 1;
            }

            if( error )
            {
                event.setLine( 0, "" );
                event.setLine( 1, "" );
                event.setLine( 2, "" );
                event.setLine( 3, "" );
                return;
            }

            ChatColor color = colors.get( random.nextInt( colors.size() ) );

            event.setLine( 0, color + "[Prefix]" );
            event.setLine( 1, ChatColor.DARK_GRAY + prefix );
            event.setLine( 2, "" );
            event.setLine( 3, color + "FREE" );
            if ( price > 0 )
            {
                event.setLine( 3, color + "" + price + " Ro" );
            }

            player.sendMessage( OkiCore.getPfx() + "Prefix sign created!" );
            return;
        }
    }

    @EventHandler
    public void onPlayerInteract( PlayerInteractEvent event )
    {
        if( event.isCancelled() )
        {
            return;
        }

        if( event.getPlayer() == null )
        {
            return;
        }

        Player player = event.getPlayer();

        //If the interaction is a prefix token
        if( event.getAction().equals( Action.RIGHT_CLICK_AIR ) || event.getAction().equals( Action.RIGHT_CLICK_BLOCK ) )
        {
            //Check if a sign is clicked
            if( event.getClickedBlock().getState() instanceof Sign )
            {
                Sign sign = (Sign) event.getClickedBlock().getState();
                String line1 = ChatColor.stripColor( sign.getLine( 0 ) ).toLowerCase();

                if( line1.equals( "[prefix]" ) )
                {
                    String prefix = ChatColor.stripColor( sign.getLine( 1 ) );
                    if( prefix == null || prefix.equals( "" ) || prefix.equals( " " ) )
                    {
                        player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "This sign has no prefix! Tell an admin please ;3" );
                        return;
                    }

                    String priceString = ChatColor.stripColor( sign.getLine( 3 ) );
                    int price = 0;

                    if ( !priceString.contains( "FREE" ) )
                    {
                        try
                        {
                            price = Integer.parseInt( priceString.split( " " )[0] );
                        }
                        catch ( Exception e )
                        {
                            player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "There was a price error! Tell an admin please ;3" );
                            return;
                        }
                    }

                    //Update Fe Signs..
                    if ( priceString.contains( "Fe" ) || priceString.contains( "Fé" ) )
                    {
                        price = price / 10;

                        String color = sign.getLine( 0 ).substring( 0, 2 );

                        sign.setLine( 3, color + "FREE" );
                        if ( price > 0 )
                        {
                            sign.setLine( 3, color + "" + price + " Ro" );
                        }
                    }
                    //End Update
                    if( econ.getBalance( player ) < price )
                    {
                        player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "You can't afford this prefix!" );
                        return;
                    }
                    econ.withdrawPlayer( player, price );

                    OkiCore.chat.setPlayerPrefix( player, prefix );

                    player.sendMessage( OkiCore.getPfx() + "Yay, prefix set to " + ChatColor.GRAY + prefix + ChatColor.GREEN + "!" );
                }
            }
            //Else assume its a token
            else
            {
                ItemStack i = player.getInventory().getItemInMainHand();

                if( !i.getType().equals( Material.PAPER ) )
                {
                    return;
                }

                ItemMeta im = i.getItemMeta();

                if( !im.getDisplayName().contains( "Prefix Token" ) )
                {
                    return;
                }

                List<String> lore = im.getLore();

                if( lore.size() < 3 )
                {
                    player.sendMessage( OkiCore.getPfx() + ChatColor.RED + "There's and error with that token, ask an admin!" );
                    return;
                }

                String prefix = ChatColor.stripColor( lore.get( 2 ) );

                OkiCore.chat.setPlayerPrefix( player, prefix );

                player.sendMessage( OkiCore.getPfx() + "Yay, prefix set to " + ChatColor.GRAY + prefix + ChatColor.GREEN + "!" );

                useItem( player );
            }
        }
    }

    private void useItem( Player player )
    {
        ItemStack handItems = player.getInventory().getItemInMainHand();

        if( handItems.getAmount() > 1 )
        {
            handItems.setAmount( handItems.getAmount() - 1 );
            player.getInventory().setItemInMainHand( handItems );
            return;
        }

        player.getInventory().setItemInMainHand( new ItemStack( Material.AIR ) );
    }
}
