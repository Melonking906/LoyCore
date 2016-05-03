package com.okicraft.okicore.death;

public class DeathRunnable implements Runnable
{
    private Death death;

    public DeathRunnable( Death death )
    {
        this.death = death;
    }

    @Override
    public void run()
    {
        death.refreshDeadPlayers();
    }
}