package com.okicraft.okicore;

import com.okicraft.okicore.autopromote.AutoPromote;
import io.loyloy.nicky.Nick;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_9_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;

public class JoinLeaveListener implements Listener
{
    private final OkiCore plugin;
    private final SendPacketThread sendThread;

    private static final String MOLJOIN = ChatColor.GRAY + "Join " + ChatColor.AQUA + "Molly " + ChatColor.GREEN + "✕ " + ChatColor.GRAY;
    private static final String MOLLEAVE = ChatColor.GRAY + "Leave " + ChatColor.AQUA + "Molly " + ChatColor.GREEN + "✕ " + ChatColor.GRAY;

    private static HashMap<String, String> messages = new HashMap<>();

    private static String[] greetings = { "That drop!",
            "Have You Seen Molly?",
            "Let's Roll!",
            "Booyah!",
            "Whatever.",
            "As If!",
            "Whatever Trevor",
            "Yum Yum Pigs Bum",
            "Go Home Roger!",
            "I Double Dare You.",
            "Who Loves Orange Soda?",
            "Score!",
            "Dibs!",
            "Sup, b?",
            "Bling-Bling",
            "Damn Skippy",
            "Sup Home Skillet",
            "OMFG",
            "Yo, yo, homie Joe",
            "How's it hanging?",
            "Shazing!",
            "Totally Tubular!",
            "Where's The Beef?",
            "You look mahhhvellous!",
            "Frankie says RELAX",
            "Like, Oh My God!",
            "How much longer Papa Smurf?",
            "Look Ma! I Caught a Fraggle!",
            "Lordy, Lordy, Lordy!",
            "Mama Mia!",
            "Meep-Meep!",
            "Resistance is futile!",
            "1.21 GIGAWATTS!",
            "Let's Rock!!!",
            "Great Scott!",
            "Bubble Yum keeps it poppin!",
            "Wocka, Wocka, Wocka",
            "Zoinks!",
            "Jinkys!",
            "Righteous!",
            "Live loy and prosper!"};

    public JoinLeaveListener( OkiCore plugin )
    {
        this.plugin = plugin;
        this.sendThread = new SendPacketThread();

        reloadMessages( plugin );
    }

    @EventHandler
                    public void onJoin( PlayerJoinEvent evt )
                    {
                        evt.setJoinMessage( null );
                        final Player player = evt.getPlayer();

                        if( player == null )
                        {
                            return;
                        }

                        sendMotd( player );
                        updateTabHF( false );

                        plugin.db.updatePlayer( player );

                        if( player.hasPermission( "loy.fly" ) )
                        {
                            player.setAllowFlight( true );
                            player.setFlying( true );
                        }

                        if( !player.hasPermission( "loy.gmanyworld" ) )
                        {
                            player.setGameMode( GameMode.SURVIVAL );
                        }

                        new BukkitRunnable()
                        {
                            public void run()
                            {
                            }

                        }.runTaskLater( plugin, 60L );

                        if( !player.isOnline() )
                        {
                            return;
                        }

                        String name;
                        String displayName = ChatColor.stripColor( player.getDisplayName() );
                        displayName = displayName.replace( "_", "" );
                        displayName = displayName.replace( "Mr", "" );
                        displayName = displayName.replace( "Sir", "" );
                        displayName = displayName.replace( "The", "" );
                        displayName = displayName.replace( "X", "" );
                        displayName = displayName.replace( "x", "" );
                        int cutLength = 3;

                        for ( int i=1 ; i < displayName.length() ; i++ )
                        {
                            if ( Character.isUpperCase( displayName.codePointAt( i ) ) || !Character.isAlphabetic( displayName.codePointAt( i ) ) )
                            {
                                cutLength = i;
                                break;
                }
            }

            if ( displayName.length() >= cutLength )
            {
                name = displayName.substring( 0, cutLength );
            }
            else
            {
                name = displayName;
            }

            player.sendMessage( OkiCore.getMol() + "Sup " + ChatColor.YELLOW + name + ChatColor.WHITE + "! Welcome to Loy ;3" );

            if( !OkiCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
            {
                return;
            }

            for ( Player onlinePlayer : Bukkit.getOnlinePlayers() ) {
                if (!onlinePlayer.equals(player)) {
                    onlinePlayer.sendMessage(getJoinMessage( player));
                }
            }


        new BukkitRunnable()
        {
            public void run()
            {
                if( !player.isOnline() )
                {
                    return;
                }
               // Old Jolicraft Pack (1.8)
               // player.setResourcePack( "http://www.loyloy.io/filestore/jolicraft1.8.zip" );
                // Faithful 1.8
               // player.setResourcePack( "https://dl.dropbox.com/s/s8erbqp06t3swl7/faithful-1.8-edit.zip" );

                // Faithful 1.9
                player.setResourcePack( "https://dl.dropbox.com/s/aj6f44s5q9kprhl/faithful-1.9-edit.zip" );

            }
        }.runTaskLater( plugin, 400L ); //Run after 20 seconds
    }

    @EventHandler
    public void onQuit( PlayerQuitEvent evt )
    {
        evt.setQuitMessage( null );
        Player player = evt.getPlayer();
        Nick nick = new Nick( player );

        plugin.db.updatePlayer( player );

        if( !OkiCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
        {
            return;
        }

        String name = nick.get();
        if( name != null )
        {
            name = nick.format( name );
        }
        else
        {
            name = player.getDisplayName();
        }

        for ( Player onlinePlayer : Bukkit.getOnlinePlayers() )
        {
            if ( !onlinePlayer.equals( player ) )
            {
                onlinePlayer.sendMessage( getLeaveMessage( player ) );
            }
        }

        updateTabHF( true );
    }

    @EventHandler
    public void onPing( ServerListPingEvent event )
    {
        String motd = messages.get( "ping" );

        if( motd.equals( "{random}" ) )
        {
            int idx = new Random().nextInt( greetings.length );
            motd = greetings[idx];
        }

        motd = ChatColor.GRAY + motd + "\n"+ChatColor.GRAY+"*- "+ChatColor.AQUA+"OkiCraft"+ChatColor.GRAY+" -*";

        event.setMotd( motd );
    }

    public static void sendMotd( Player player )
    {
        String playerName = player.getDisplayName();

        int idx = new Random().nextInt( greetings.length );
        String greeting = greetings[idx];

        //Send MOTD
        player.sendMessage( "" );
        player.sendMessage( "§b§m---------------------------------------------------" ); //Strike
        player.sendMessage( "§3Welcome to Oki, §f" + playerName + " §3❤" );
        player.sendMessage( "§b§m---------------------------------------------------" ); //Strike
        player.sendMessage( "§aNews: §f" + messages.get( "newsLine1" ) );
        player.sendMessage( "§f" + messages.get( "newsLine2" ) );
        player.sendMessage( "" );

        // Inject screen title.
        String rawTitle = "§7*- §b" + greeting + " §7-*";
        String rawSubTitle = "§aWelcome to Oki :3 " + playerName;

        TitleMessage.showMessage( player, rawTitle, rawSubTitle, 60 );
    }

    public void updateTabHF( boolean isLogout )
    {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        int count = players.size();

        if( isLogout )
        {
            count--;
        }

        String rawFooter = ChatColor.GRAY + "*- " + ChatColor.YELLOW + count + " Peeps Online" + ChatColor.GRAY + " -*";

        IChatBaseComponent header = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + messages.get( "header" ) + "\"}" );
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + rawFooter + "\"}" );
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        try
        {
            Field headerField = packet.getClass().getDeclaredField( "a" );
            headerField.setAccessible( true );
            headerField.set( packet, header );
            headerField.setAccessible( !headerField.isAccessible() );

            Field footerField = packet.getClass().getDeclaredField( "b" );
            footerField.setAccessible( true );
            footerField.set( packet, footer );
            footerField.setAccessible( !footerField.isAccessible() );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        sendThread.run( players, packet );
    }

    private class SendPacketThread extends Thread
    {
        public void run( Collection<? extends Player> players, PacketPlayOutPlayerListHeaderFooter packet )
        {
            for( Player player : players )
            {
                CraftPlayer craftplayer = (CraftPlayer) player;
                PlayerConnection connection = craftplayer.getHandle().playerConnection;

                connection.sendPacket( packet );
            }
        }
    }

    public static void reloadMessages( OkiCore plugin )
    {
        messages.clear();

        messages.put( "newsLine1", plugin.getConfig().getString( "newsLine1" ) );
        messages.put( "newsLine2", plugin.getConfig().getString( "newsLine2" ) );
        messages.put( "header", plugin.getConfig().getString( "tab_header" ) );
        messages.put( "ping" , plugin.getConfig().getString( "ping_motd" ) );

        for( Map.Entry<String, String> message : messages.entrySet() )
        {
            message.setValue( ChatColor.translateAlternateColorCodes( '&', message.getValue() ) );
            messages.put( message.getKey(), message.getValue() );
        }
    }
    public static String getJoinMessage( Player p ) { return MOLJOIN + ChatColor.stripColor( p.getDisplayName() ) +  " is in! :3"; }

    public static String getLeaveMessage( Player p ) { return MOLLEAVE + ChatColor.stripColor( p.getDisplayName() ) + " is out the door! 3:"; }

}