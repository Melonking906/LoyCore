package com.okicraft.okicore;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
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