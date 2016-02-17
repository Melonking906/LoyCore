package me.nonit.loycore.chat;

import com.massivecraft.factions.entity.MPlayer;
import me.nonit.loycore.EmeraldEcon;
import me.nonit.loycore.LoyCore;
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
    private static final Chat chat = LoyCore.chat;
    private static final Permission perm = LoyCore.permission;

    private final SendPacketThread sendThread;

    private ChannelStore cs;
    private Random random;
    private List<String> prefixes;

    public ChatListener( LoyCore plugin, ChannelStore cs )
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

        if( ! perm.playerInGroup( p, "builder" ) )
        {
            p.sendMessage( LoyCore.getPfx() + ChatColor.GRAY + "Please wait to be promoted for chat to enable ^-^" );
            return;
        }

        ChatColor msgColor = ChatColor.WHITE;
        boolean isLocal = false;
        boolean isStaff = false;

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
            default:
                break;
        }

        String name = p.getDisplayName();
        String prefix = chat.getPlayerPrefix( p );
        if( prefix.equals( "{random}" ) )
        {
            prefix = prefixes.get( random.nextInt( prefixes.size() ) );
        }

        MPlayer factionPlayer = MPlayer.get( p );
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
        }
//        if( prefix.equals( "{fe}" ) )
//        {
//            prefix = LoyCore.economy.format( LoyCore.economy.getBalance( p ) );
//        }

        String suffix = chat.getPlayerSuffix( p );

        String group = perm.getPrimaryGroup( p ).substring( 0, 1 ).toUpperCase() + perm.getPrimaryGroup( p ).substring( 1 );

        String msg = e.getMessage();

        //Name Tooltip
        String nameToolTip = "";

        String faction = "none...";
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

        //Color fix
        msg = StringWrapping.wrapString( msgColor + msg, '§', 80, false );

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

        //Island only chat
        if( isLocal )
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
            for( Player receiver : onlinePlayers )
            {
                if( perm.playerInGroup( receiver, "mod" ) || perm.playerInGroup( receiver, "admin" ) )
                {
                    recipients.add( receiver );
                }
            }
        }
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
