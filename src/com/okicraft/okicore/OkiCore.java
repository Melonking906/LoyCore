package com.okicraft.okicore;

import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.bukkit.IslandCraftPlugin;
import com.okicraft.okicore.autopromote.AutoPromote;
import com.okicraft.okicore.chat.ChannelStore;
import com.okicraft.okicore.chat.ChatCommand;
import com.okicraft.okicore.chat.ChatListener;
import com.okicraft.okicore.chat.MollyChat;
import com.okicraft.okicore.commands.*;
import com.okicraft.okicore.database.MySQL;
import com.okicraft.okicore.database.SQL;
import com.okicraft.okicore.prefix.PrefixListener;
import com.okicraft.okicore.pvp.PvP;
import com.okicraft.okicore.pvp.PvPCommand;
import com.okicraft.okicore.pvp.PvPListener;
import com.okicraft.okicore.autopromote.AutoPromote;
import com.okicraft.okicore.database.MySQL;
import com.okicraft.okicore.database.SQL;
import com.okicraft.okicore.prefix.PfxTokenCommand;
import com.okicraft.okicore.prefix.PrefixListener;
import com.okicraft.okicore.pvp.PvP;
import com.okicraft.okicore.pvp.PvPCommand;
import com.okicraft.okicore.pvp.PvPListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class OkiCore extends JavaPlugin
{
    public static Permission permission = null;
    public static Chat chat = null;
    public IslandCraft islandCraft = null;

    public SQL db;

    private static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.AQUA + "Oki" + ChatColor.GRAY + "]" + ChatColor.GREEN + " ";
    private static final String MOLLY = ChatColor.GRAY + "Bot " + ChatColor.AQUA + "Molly " + ChatColor.GREEN + "✕" + ChatColor.WHITE + " ";

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig(); // Makes a config if one does not exist.

        setupPermissions();
        setupChat();
        setupIslandCraft();

        this.db = new MySQL( this );
        //this.db = new SQLite( this );

        PluginManager pm = getServer().getPluginManager();

        getCommand( "motd" ).setExecutor( new MotdCommand() );
        getCommand( "hug" ).setExecutor( new HugCommand() );
        getCommand( "setnews" ).setExecutor( new SetNewsCommand( this ) );
        getCommand( "unsign" ).setExecutor( new UnSignCommand() );
        getCommand( "fix" ).setExecutor( new FixCommand() );
        getCommand( "loyreload" ).setExecutor( new LoyReloadCommand( this ) );
        getCommand( "announce" ).setExecutor( new AnnounceCommand( this ) );
        getCommand( "alert" ).setExecutor( new AlertCommand() );
        getCommand( "kickemall" ).setExecutor( new KickEmAllCommand() );
        getCommand( "giveeveryone" ).setExecutor( new GiveEveryoneCommand() );
        getCommand( "mollytalk" ).setExecutor( new MollyTalkCommand() );
        getCommand( "send" ).setExecutor( new SendCommand() );
        getCommand( "fly" ).setExecutor( new FlyCommand() );
        getCommand( "seen" ).setExecutor( new SeenCommand( this ) );
        getCommand( "emeralds" ).setExecutor( new EmeraldsCommand() );

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        //Loy Chat module
        ChannelStore channelStore = new ChannelStore();
        ChatListener chatListener = new ChatListener( channelStore );
        pm.registerEvents( chatListener, this );
        pm.registerEvents( new MollyChat( this, channelStore ), this );
        getCommand( "chat" ).setExecutor( new ChatCommand( channelStore ) );
        getCommand( "playertalk" ).setExecutor( new PlayerTalkCommand( chatListener ) );


        // PvP
        PvP pvp = new PvP();
        PvPListener pvPListener = new PvPListener( pvp );
        pm.registerEvents( pvPListener, this );
        getCommand( "pvp" ).setExecutor( new PvPCommand( pvPListener, pvp ) );

        //Prefix Stuff
        pm.registerEvents( new PrefixListener(), this );
        getCommand( "prefixtoken" ).setExecutor( new PfxTokenCommand() );

        // Pocket money
        scheduler.scheduleSyncRepeatingTask(this, new PayRunnable(), 35000L, 72000L ); // Every hour

        // Announcements
        if( getConfig().getStringList( "announcements" ).size() >= 2 )
        {
            scheduler.scheduleSyncRepeatingTask( this, new AnnounceRunnable( this ), 25000L, 25000L );
        }
        else
        {
            getLogger().info( "Please enter at least 2 announcements for announcements to enable!" );
        }

        // Auto Promote
        new AutoPromote( this );

        //Gamemode manager
        pm.registerEvents( new GameModesListener(), this );

        // Votifier
        if( pm.getPlugin( "Votifier" ) != null )
        {
            pm.registerEvents( new VoteListener(), this );
        }

        //Rain Drops
        scheduler.scheduleSyncRepeatingTask( this, new RaindropsRunnable(), 600L, 600L );

        //Claw Games
        //scheduler.scheduleSyncRepeatingTask( this, new ClawRunnable(), 1L, 1L );

        //Stuff
        pm.registerEvents( new JoinLeaveListener( this ), this );
        pm.registerEvents( new EggDropListener(), this );
        pm.registerEvents( new DontBuildListener(), this );
    }

    @Override
    public void onDisable()
    {
        db.disconnect();
        islandCraft = null;
    }

    public static String getPfx() { return PREFIX; }

    public static String getMol() { return MOLLY; }

    private void setupIslandCraft()
    {
        try {
            final IslandCraftPlugin islandCraftPlugin = getPlugin(IslandCraftPlugin.class);
            islandCraft = islandCraftPlugin.getIslandCraft();
        } catch (final Exception e) {
            getLogger().severe("Could not find IslandCraft, please make sure plugin is installed correctly.");
            setEnabled(false);
            return;
        }
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    public static List<Player> getOnlineStaff()
    {
        List<Player> staff = new ArrayList<>();

        for( Player receiver : Bukkit.getOnlinePlayers() )
        {
            if( permission.playerInGroup( receiver, "mod" ) || permission.playerInGroup( receiver, "admin" ) )
            {
                staff.add( receiver );
            }
        }

        return staff;
    }

    public static void staffBroadcast( String msg )
    {
        for( Player p : getOnlineStaff() )
        {
            p.sendMessage( ChatColor.RED + "[Staff] " + ChatColor.GRAY + msg );
        }
    }


}
