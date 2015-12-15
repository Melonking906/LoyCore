package me.nonit.loycore;

import io.loyloy.nicky.Nick;
import me.nonit.loycore.autopromote.AutoPromote;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
    private final LoyCore plugin;
    private List<String> blockedPlayers;
    private final SendPacketThread sendThread;

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

    public JoinLeaveListener( LoyCore plugin )
    {
        this.plugin = plugin;
        this.sendThread = new SendPacketThread();

        reloadMessages( plugin );

        blockedPlayers = new ArrayList<>();
        blockedPlayers.add( "e88ecb4d643c483fa6c12e7c4c3e59d1" );
        blockedPlayers.add( "12215a408c9e410f902c9383533640d6" );
        blockedPlayers.add( "02219d7e13824a3fad362ab8ddfe5bfa" );
        blockedPlayers.add( "69876f77e2a94b0a82c5f66f120d5e5a" );
        blockedPlayers.add( "a81cfa83fc1348a8950a0af073d19329" );
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

        String playerUUID = player.getUniqueId().toString().replaceAll( "-", "" );
        if( blockedPlayers.contains( playerUUID ) )
        {
            new PlayerQuitEvent( player, "com.avaje.ebeaninternal.server.lib.thread.PooledThread.run(PooledThread.java)" );
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

        new BukkitRunnable()
        {
            public void run()
            {
                if( player.isOnline() )
                {
                    if( LoyCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
                    {
                        String loginMessage = messages.get( "login" ).replace( "{player}", player.getDisplayName() );
                        Bukkit.broadcastMessage( loginMessage );
                    }
                }
            }
        }.runTaskLater( plugin, 2L );
    }

    @EventHandler
    public void onQuit( PlayerQuitEvent evt )
    {
        evt.setQuitMessage( null );
        Player player = evt.getPlayer();

        Nick nick = new Nick( player );
        String name = nick.get();
        if( name != null )
        {
            name = nick.format( name );
        }
        else
        {
            name = player.getDisplayName();
        }

        plugin.db.updatePlayer( player );

        if( LoyCore.permission.playerInGroup( player, AutoPromote.PROMOTE_RANK ) )
        {
            String logoutMessage = messages.get( "logout" ).replace( "{player}", name );
            Bukkit.broadcastMessage( logoutMessage );
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

        motd = ChatColor.GRAY + motd + "\n"+ChatColor.GRAY+"*- "+ChatColor.YELLOW+"LoyLoy.io"+ChatColor.GRAY+" -*";

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
        player.sendMessage( "§3Welcome to Loy §f " + playerName + " §3❤" );
        player.sendMessage( "§b§m---------------------------------------------------" ); //Strike
        player.sendMessage( "§aNews: §f" + messages.get( "newsLine1" ) );
        player.sendMessage( "§f" + messages.get( "newsLine2" ) );
        player.sendMessage( "" );

        // Inject screen title.
        String rawTitle = "§7*- §b" + greeting + " §7-*";
        String rawSubTitle = "§aWelcome to Loy " + playerName;

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

    public static void reloadMessages( LoyCore plugin )
    {
        messages.clear();

        messages.put( "login", plugin.getConfig().getString( "login" ) );
        messages.put( "logout", plugin.getConfig().getString( "logout" ) );
        messages.put( "newsLine1", plugin.getConfig().getString( "newsLine1" ) );
        messages.put( "newsLine2", plugin.getConfig().getString( "newsLine2" ) );
        messages.put( "header", plugin.getConfig().getString( "tab_header" ) );
        messages.put( "footer", plugin.getConfig().getString( "tab_footer" ) );
        messages.put( "ping" , plugin.getConfig().getString( "ping_motd" ) );

        for( Map.Entry<String, String> message : messages.entrySet() )
        {
            message.setValue( ChatColor.translateAlternateColorCodes( '&', message.getValue() ) );
            messages.put( message.getKey(), message.getValue() );
        }
    }
}