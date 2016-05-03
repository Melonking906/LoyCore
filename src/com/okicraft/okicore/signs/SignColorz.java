package com.okicraft.okicore.signs;

import com.okicraft.okicore.OkiCore;
import org.bukkit.ChatColor;

/**
 * Created by skitt on 5/2/2016.
 */
public class SignColorz {
        // REFERENCES
        private static SignColorz	instance;

        // Messages
        public static final String permissions_message = OkiCore.getPfx() + "Sorry, you don't have permission for that!";

        // CONSTRUCTOR
        public SignColorz()
        {
            instance = this;
        }


    // GETTERS
    public static SignColorz getInstance()
    {
        return instance;
    }


    // CHAT COLORS
    public static String translateColorCodes(String s)
    {
        s = ChatColor.translateAlternateColorCodes('&', s);
        s = ChatColor.translateAlternateColorCodes('$', s);
        return s;
    }

    public static String translateColorCodesWithoutMagic(String s)
    {
        s = s.replace("&k", "").replace("$k", "");

        s = ChatColor.translateAlternateColorCodes('&', s);
        s = ChatColor.translateAlternateColorCodes('$', s);
        return s;
    }

    public static boolean containsColorCodes(String s)
    {
        if(translateColorCodes(s).equals(s))
            return false;

        return true;
    }

    public static boolean containsMagic(String s)
    {
        if(s.contains("&k") || s.contains("$k"))
            return true;

        return false;
    }
}
