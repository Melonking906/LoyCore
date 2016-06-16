package com.okicraft.okicore;

import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R2.PlayerConnection;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleMessage
{
    private static final int FADE_IN = 9;
    private static final int FADE_OUT = 9;

    public static void showMessage( Player player, String title, String subTitle, int time, int fadeIn, int fadeOut )
    {
        PlayerConnection craftPlayer = ((CraftPlayer )player).getHandle().playerConnection;

        PacketPlayOutTitle titleBig = new PacketPlayOutTitle( PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}"), fadeIn, time, fadeOut );
        PacketPlayOutTitle titleSmall = new PacketPlayOutTitle( PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subTitle + "\"}"), fadeIn, time, fadeOut );

        craftPlayer.sendPacket(titleBig);
        craftPlayer.sendPacket(titleSmall);
    }

    public static void showMessage( Player player, String title, String subTitle, int time )
    {
        showMessage( player, title, subTitle, time, FADE_IN, FADE_OUT );
    }
}