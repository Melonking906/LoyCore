package me.nonit.loycore;

import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.bukkit.IslandCraftPlugin;
import io.loyloy.fe.API;
import io.loyloy.fe.Fe;
import me.nonit.loycore.autopromote.AutoPromote;
import me.nonit.loycore.chat.ChannelStore;
import me.nonit.loycore.chat.ChatCommand;
import me.nonit.loycore.chat.ChatListener;
import me.nonit.loycore.chat.IRCManager;
import me.nonit.loycore.commands.*;
import me.nonit.loycore.database.MySQL;
import me.nonit.loycore.database.SQL;
import me.nonit.loycore.prefix.PfxTokenCommand;
import me.nonit.loycore.prefix.PrefixListener;
import me.nonit.loycore.pvp.PvPCommand;
import me.nonit.loycore.pvp.PvPListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class LoyCore extends JavaPlugin
{
    public static Economy economy = null;
    public static Permission permission = null;
    public static Chat chat = null;
    public IslandCraft islandCraft = null;

    public SQL db;
    public API fe = null;

    private static final String PREFIX = ChatColor.YELLOW + "[Loy]" + ChatColor.GREEN + " ";

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig(); // Makes a config is one does not exist.

        setupEconomy();
        setupPermissions();
        setupChat();
        setupFeLink();
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
        getCommand( "mollytalk" ).setExecutor( new MollyTalkCommand( this ) );
        getCommand( "send" ).setExecutor( new SendCommand( this ) );
        getCommand( "fly" ).setExecutor( new FlyCommand() );
        getCommand( "seen" ).setExecutor( new SeenCommand( this ) );

        if( pm.getPlugin( "EchoPet" ) != null )
        {
            getCommand( "addpet" ).setExecutor( new AddPetCommand() );
        }

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        //Loy Chat module
        ChannelStore channelStore = new ChannelStore();
        ChatListener chatListener = new ChatListener( this, channelStore );
        pm.registerEvents( chatListener, this );
        getCommand( "chat" ).setExecutor( new ChatCommand( channelStore ) );
        getCommand( "playertalk" ).setExecutor( new PlayerTalkCommand( chatListener ) );
        pm.registerEvents( new IRCManager( channelStore ), this );

        //Stuff
        pm.registerEvents( new JoinLeaveListener( this ), this );
        pm.registerEvents( new MollyChat( this ), this );
        pm.registerEvents( new EggDropListener(), this );
        pm.registerEvents( new DontBuildListener(), this );

        // PvP
        PvPListener pvPListener = new PvPListener();
        pm.registerEvents( pvPListener, this );
        getCommand( "pvp" ).setExecutor( new PvPCommand( pvPListener ) );

        // Inv Saver
        InvSaverListener saverListener = new InvSaverListener( this );
        pm.registerEvents( saverListener, this );
        getCommand( "invclaim" ).setExecutor( new InvClaimCommand( saverListener ) );

        //Prefix Stuff
        pm.registerEvents( new PrefixListener(), this );
        getCommand( "prefixtoken" ).setExecutor( new PfxTokenCommand() );

        // Pocket money
        scheduler.scheduleSyncRepeatingTask( this, new PayRunnable( this ), 35000L, 72000L ); // Every hour

        // Announcments
        if( getConfig().getStringList( "announcements" ).size() >= 2 )
        {
            scheduler.scheduleSyncRepeatingTask( this, new AnnounceRunnable( this ), 25000, 25000 );
        }
        else
        {
            getLogger().info( "Please enter at least 2 announcements for announcements to enable!" );
        }

        // Auto Promote
        new AutoPromote( this );

        //Gamemode managr
        pm.registerEvents( new GameModesListener(), this );

        // Votifier
        if( pm.getPlugin( "Votifier" ) != null )
        {
            pm.registerEvents( new VoteListener( this ), this );
        }
    }

    @Override
    public void onDisable()
    {
        db.disconnect();
        islandCraft = null;
    }

    public static String getPfx() { return PREFIX; }

    private void setupFeLink()
    {
        try
        {
            final Fe fePlugin = getPlugin( Fe.class );
            this.fe = fePlugin.getAPI();

        }
        catch( final Exception e )
        {
            getLogger().severe( "Could not find Fe, please make sure plugin is installed correctly." );
            setEnabled( false );
        }
    }

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

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
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
}
