//package me.nonit.loycore.chat;
//
//import me.nonit.loycore.LoyCore;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.AsyncPlayerChatEvent;
//import org.jibble.pircbot.PircBot;
//
//public class IRCManager implements Listener
//{
//    private final ChannelStore channelStore;
//
//    private static final String server = "loyloy.io";
//    private static final String password = "teatime";
//
//    private final PircBot bot;
//
//    public IRCManager( ChannelStore channelStore )
//    {
//        this.channelStore = channelStore;
//
//        bot = new MollyBot();
//
//        try
//        {
//            bot.connect( server, 6667, password );
//        }
//        catch ( Exception e )
//        {
//            Bukkit.getLogger().info( "Could not connect to IRC server..." );
//            return;
//        }
//
//        bot.joinChannel( "#staff" );
//        bot.joinChannel( "#chat" );
//    }
//
//    @EventHandler( priority= EventPriority.LOWEST )
//    public void onChat( AsyncPlayerChatEvent e )
//    {
//        if ( e.isCancelled() )
//        {
//            return;
//        }
//
//        String name = e.getPlayer().getName();
//        String message = ChatColor.stripColor( e.getMessage() );
//
//        char channel = channelStore.getPlayerChannel( e.getPlayer() );
//
//        if ( channel == 'g' )
//        {
//            bot.sendMessage( "#chat", "<"+name+"> " + message );
//            return;
//        }
//
//        if ( channel == 's' )
//        {
//            bot.sendMessage( "#staff", "<"+name+"> " + message );
//            return;
//        }
//    }
//
//    private class MollyBot extends PircBot
//    {
//        public MollyBot()
//        {
//            this.setName( "Molly" );
//        }
//
//        @Override
//        protected void onMessage( String channel, String sender, String login, String hostname, String message )
//        {
//            if ( channel.equals( "#staff" ) )
//            {
//                for ( Player player : Bukkit.getOnlinePlayers() )
//                {
//                    if ( LoyCore.permission.playerInGroup( player, "mod" )
//                            || LoyCore.permission.playerInGroup( player, "admin" ) )
//                    {
//                        player.sendMessage( ChatColor.GRAY + "Loy-Net " + ChatColor.WHITE + sender + ChatColor.DARK_RED + " >>> " + ChatColor.RED + message );
//                    }
//            }
//
//                return;
//            }
//
//            if ( channel.equals( "#chat" ) )
//            {
//                sendMessage( channel, "** Server people cant hear you here! **" );
//                return;
//            }
//        }
//    }
//}
