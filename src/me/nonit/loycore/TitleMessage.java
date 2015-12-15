package me.nonit.loycore;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleMessage
{
    private static final int FADE_IN = 9;
    private static final int FADE_OUT = 9;

    public static void showMessage( Player player, String title, String subTitle, int time )
    {
        PlayerConnection craftPlayer = ((CraftPlayer )player).getHandle().playerConnection;

        PacketPlayOutTitle titleBig = new PacketPlayOutTitle( PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a( "{'text': ''}" ).a(title), FADE_IN, time, FADE_OUT );
        PacketPlayOutTitle titleSmall = new PacketPlayOutTitle( PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a( "{'text': ''}" ).a(subTitle), FADE_IN, time, FADE_OUT );

        craftPlayer.sendPacket(titleBig);
        craftPlayer.sendPacket(titleSmall);
    }
}