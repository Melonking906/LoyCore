package me.nonit.loycore;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionMessage
{
    public static void showMessage( Player player, String message )
    {
        CraftPlayer p = (CraftPlayer )player;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a( "{\"text\": \"" + message.replaceAll( "&", "§" ) + "\"}" );
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
        p.getHandle().playerConnection.sendPacket(ppoc);
    }
}