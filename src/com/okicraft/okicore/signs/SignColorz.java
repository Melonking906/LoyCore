package com.okicraft.okicore.signs;

import org.bukkit.ChatColor;


public class SignColorz {

    // REFERENCES
    private static SignColorz	instance;

    // CONFIG VALUES
    public static String		permissions_message;
    public static String		magic_permissions_message;

    public static boolean		permission_based;
    public static boolean		magic_permission_based;


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
