package com.okicraft.okicore.chat;

import com.okicraft.okicore.EmeraldEcon;
import com.okicraft.okicore.OkiCore;
import net.md_5.bungee.api.chat.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ChatListener implements Listener
{
    private static final Chat chat = OkiCore.chat;
    private static final Permission perm = OkiCore.permission;

    private final SendPacketThread sendThread;

    private ChannelStore cs;
    private Random random;
    private List<String> prefixes;

    public ChatListener( ChannelStore cs )
    {
        this.sendThread = new SendPacketThread();
        this.cs = cs;
        this.random = new Random();

        this.prefixes = new ArrayList<>();
        prefixes.add( "Pirate" );
        prefixes.add( "Squid" );
        prefixes.add( "Lunar" );
        prefixes.add( "Trojan" );
        prefixes.add( "Mush" );
        prefixes.add( "Tree" );
        prefixes.add( "Confused" );
        prefixes.add( "Nifty" );
        prefixes.add( "Tricky" );
        prefixes.add( "Lumpy" );
        prefixes.add( "Lokish" );
        prefixes.add( ChatColor.DARK_AQUA + "F" + ChatColor.AQUA + "i" + ChatColor.WHITE + "s" + ChatColor.AQUA + "h" );
        prefixes.add( "Dark" );
        prefixes.add( "Moose" );
        prefixes.add( "Panda" );
        prefixes.add( "Stone" );
        prefixes.add( "Misty" );
        prefixes.add( "Spark" );
    }

    @EventHandler( priority= EventPriority.LOW )
    public void onChat( AsyncPlayerChatEvent e )
    {

        if ( e.isCancelled() )
        {
            return;
        }
        e.setCancelled(true);

        Player p = e.getPlayer();
        if( p == null )
        {
            return;
        }

/*
        if(ic.isIgnored( p ))
        {

        }
*/

        ChatColor msgColor = ChatColor.WHITE;
        boolean isLocal = false;
        boolean isStaff = false;
       //  boolean isWorld = false; // Is this player in world-specific chat?
        switch( cs.getPlayerChannel( p ) )
        {
            case 'l':
                msgColor = ChatColor.YELLOW;
                isLocal = true;
                break;
            case 's':
                msgColor = ChatColor.RED;
                isStaff = true;
                break;
 /*           case 'w':
                msgColor = ChatColor.LIGHT_PURPLE;
                isWorld = true;
                break;
 */
            default:
                break;
        }


        String name = p.getDisplayName();
        String prefix = chat.getPlayerPrefix( p );
        if( prefix.equals( "{random}" ) )
        {
            prefix = prefixes.get( random.nextInt( prefixes.size() ) );
        }


        // Disable faction prefixes, no factions.
/*        MPlayer factionPlayer = MPlayer.get( p );
        if ( prefix.equals( "{faction}" ) )
        {
            if ( factionPlayer.hasFaction() )
            {
                prefix = ChatColor.DARK_AQUA + factionPlayer.getFactionName();
            }
            else
            {
                prefix = ChatColor.DARK_AQUA + "Factionless";
            }
        }*/

        String suffix = chat.getPlayerSuffix( p );

        String group = perm.getPrimaryGroup( p ).substring( 0, 1 ).toUpperCase() + perm.getPrimaryGroup( p ).substring( 1 );

        String msg = e.getMessage();

        //Name Tooltip
        String nameToolTip = "";

        // More factions stuff off.
 /*       String faction = "none...";
        String title = "none...";
        if ( factionPlayer.hasFaction() )
        {
            faction = factionPlayer.getFactionName();
        }
        if ( factionPlayer.hasTitle() )
        {
            title = factionPlayer.getTitle();
        }
        nameToolTip += ChatColor.RED + "Faction " + ChatColor.GRAY + faction;
        nameToolTip += "\n" + ChatColor.GOLD + "Title " + ChatColor.GRAY + title;
*/
        nameToolTip += "\n" + ChatColor.GREEN + "Emeralds " + ChatColor.GRAY + EmeraldEcon.getBalance( p );
        nameToolTip += "\n" + ChatColor.WHITE + "Name " + ChatColor.GRAY + p.getName();

        if( p.hasPermission( "loy.chat.color" ) )
        {
            msg = ChatColor.translateAlternateColorCodes( '&', msg );
        }
        else
        {
            msg = ChatColor.stripColor( msg );
        }

        TextComponent tcPrefix = new TextComponent( ChatColor.GRAY + prefix );

        TextComponent tcSuffix = new TextComponent( ChatColor.GREEN + suffix );
        tcSuffix.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder( ChatColor.GREEN + group ).create() ) );

        TextComponent tcName = new TextComponent( ChatColor.YELLOW + name );
        tcName.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder( nameToolTip ).create() ) );

        List<String> links = getLinks(msg);
        if( links.size() > 0 )
        {
            msg = msg.replace( links.get(0), ChatColor.UNDERLINE + "Click Meh" + msgColor );
        }

        msg = msgColor + msg;

        TextComponent tcMessage = new TextComponent( msg );
        if( links.size() > 0 )
        {
            tcMessage.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, links.get(0) ) );
            tcMessage.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( links.get(0) ).create() ) );
        }

        TextComponent tcFinal = new TextComponent( "" );
        tcFinal.addExtra( tcPrefix );
        tcFinal.addExtra( " " );
        tcFinal.addExtra( tcName );
        tcFinal.addExtra( " " );
        tcFinal.addExtra( tcSuffix );
        tcFinal.addExtra( " " );
        tcFinal.addExtra( tcMessage );

        System.out.print( "<" + p.getName() + ">: " + msg ); //Print to console

        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        List<Player> recipients = new ArrayList<>();

        if( ! perm.playerInGroup( p, "builder" ) )
        {
            p.sendMessage( ChatColor.YELLOW + "* " + ChatColor.GRAY + "Only staff can see your chat atm ^-^" );

            recipients.addAll( OkiCore.getOnlineStaff() );
            recipients.add( p );
        }
        else if( isLocal )
        {
            Location l = p.getLocation();

            int minX = l.getBlockX() - 50;
            int minZ = l.getBlockZ() - 50;
            int maxX = l.getBlockX() + 50;
            int maxZ = l.getBlockZ() + 50;

            for( Player receiver : onlinePlayers )
            {
                Location rl = receiver.getLocation();

                if( (rl.getBlockX() > minX && rl.getBlockZ() > minZ) && (rl.getBlockZ() < maxZ && rl.getBlockX() < maxX) )
                {
                    recipients.add( receiver );
                }
            }
        }
        else if( isStaff )
        {
            recipients = OkiCore.getOnlineStaff();
        }
/*        else if( isWorld )
        {
            for( Player player : onlinePlayers ) {

                if (player.getWorld() == cs.getRequestedWorld(p)) {

                    recipients.add(player);

                }
            }
        }*/
        else
        {
            for( Player receiver : onlinePlayers )
            {
                recipients.add( receiver );
            }
        }

        if( recipients.size() < 2 && !isStaff )
        {
            p.sendMessage( ChatColor.YELLOW + "* " + ChatColor.GRAY + "There is no one around to hear you..." );
        }

        sendThread.run( recipients, tcFinal );
    }

    private class SendPacketThread extends Thread
    {
        public void run( List<Player> players, BaseComponent message )
        {
            for( Player player : players )
            {
                player.spigot().sendMessage( message );
            }
        }
    }

    private static List<String> getLinks(String msg)
    {
        String[] words = msg.split( " " );

        List<String> links = new ArrayList<>();

        for( String word : words )
        {
            if( word.contains( "www." )
                    || word.contains( "http://" )
                    || word.contains( "https://" )
                    || word.contains( ".com" )
                    || word.contains( ".io" )
                    || word.contains( ".net" ) )
            {
                links.add( word );
            }
        }

        return links;
    }



}
