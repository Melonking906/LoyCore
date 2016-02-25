package me.nonit.loycore.commands;

import me.nonit.loycore.EmeraldEcon;
import me.nonit.loycore.LoyCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FixCommand implements CommandExecutor
{
    private static final int COST = 10;

    public FixCommand()
    {
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        if( sender instanceof Player )
        {
            if( sender.hasPermission( "loy.fix" ) )
            {
                Player player = ( Player ) sender;
                ItemStack berepaired = player.getItemInHand();

                if ( EmeraldEcon.getBalance( player ) < COST )
                {
                    player.sendMessage( LoyCore.getPfx() + ChatColor.RED + "You cant afford that! Its " + COST + " emeralds to fix an item." );
                    return true;
                }

                if( itemCheck( berepaired ) )
                {
                    berepaired.setDurability( ( short ) ( berepaired.getType().getMaxDurability() - berepaired.getType().getMaxDurability() ) );
                    sender.sendMessage( LoyCore.getPfx() + ChatColor.GREEN + "You have repaired the item!" );

                    EmeraldEcon.removeEmeralds( player, COST );
                }
                else
                {
                    sender.sendMessage( LoyCore.getPfx() + ChatColor.RED + "This item cannot be repaired :(" );
                }
            }
            else
            {
                sender.sendMessage( LoyCore.getPfx() + ChatColor.RED + "No permission :(" );
            }
        }

        return true;
    }

    @SuppressWarnings( "deprecation" )
    private boolean itemCheck( ItemStack w )
    {
        if( ( w.getType().getId() == 256 ) || ( w.getType().getId() == 257 ) ||
            ( w.getType().getId() == 258 ) || ( w.getType().getId() == 259 ) ||
            ( w.getType().getId() == 261 ) || ( w.getType().getId() == 267 ) ||
            ( w.getType().getId() == 268 ) || ( w.getType().getId() == 269 ) ||
            ( w.getType().getId() == 270 ) || ( w.getType().getId() == 271 ) ||
            ( w.getType().getId() == 272 ) || ( w.getType().getId() == 273 ) ||
            ( w.getType().getId() == 274 ) || ( w.getType().getId() == 275 ) ||
            ( w.getType().getId() == 276 ) || ( w.getType().getId() == 277 ) ||
            ( w.getType().getId() == 278 ) || ( w.getType().getId() == 279 ) ||
            ( w.getType().getId() == 283 ) || ( w.getType().getId() == 284 ) ||
            ( w.getType().getId() == 285 ) || ( w.getType().getId() == 286 ) ||
            ( w.getType().getId() == 290 ) || ( w.getType().getId() == 291 ) ||
            ( w.getType().getId() == 292 ) || ( w.getType().getId() == 293 ) ||
            ( w.getType().getId() == 294 ) || ( w.getType().getId() == 298 ) ||
            ( w.getType().getId() == 299 ) || ( w.getType().getId() == 300 ) ||
            ( w.getType().getId() == 301 ) || ( w.getType().getId() == 302 ) ||
            ( w.getType().getId() == 303 ) || ( w.getType().getId() == 304 ) ||
            ( w.getType().getId() == 305 ) || ( w.getType().getId() == 306 ) ||
            ( w.getType().getId() == 307 ) || ( w.getType().getId() == 308 ) ||
            ( w.getType().getId() == 309 ) || ( w.getType().getId() == 310 ) ||
            ( w.getType().getId() == 311 ) || ( w.getType().getId() == 312 ) ||
            ( w.getType().getId() == 313 ) || ( w.getType().getId() == 314 ) ||
            ( w.getType().getId() == 315 ) || ( w.getType().getId() == 316 ) ||
            ( w.getType().getId() == 317 ) || ( w.getType().getId() == 346 ) ||
            ( w.getType().getId() == 359 ) )
        {
            return true;
        }

        return false;
    }
}
