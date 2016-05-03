package com.okicraft.okicore.pvp;

import java.util.ArrayList;
import java.util.List;

public class PvP
{
    private List<String> pvpWorlds;

    public PvP()
    {
        this.pvpWorlds = new ArrayList<>();
        pvpWorlds.add( "Wild" );
        pvpWorlds.add( "Mine" );
        pvpWorlds.add( "Mine_nether" );
        pvpWorlds.add( "Mine_the_end" );
    }

    public boolean isPvPWorld(String name)
    {
        if ( pvpWorlds.contains( name ) )
        {
            return true;
        }
        return false;
    }
}
