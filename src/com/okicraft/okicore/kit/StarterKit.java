package com.okicraft.okicore.kit;

import com.okicraft.okicore.OkiCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheMIP3 on 5/27/16.
 */
public class StarterKit {

    private final OkiCore plugin;
    private final Economy econ = OkiCore.gringottsEcon;



public StarterKit( OkiCore plugin )
{

    this.plugin = plugin;

}
 private void setKitInv( Player p)
 {

     ItemStack newbieBook = new ItemStack( Material.WRITTEN_BOOK );

 }
    private void setBookMeta()
    {

        String title = ChatColor.AQUA + "Welcome to " + ChatColor.RED + "Oki" + ChatColor.GREEN + "Craft" + ChatColor.AQUA + "!";

        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bm = (BookMeta) itemStack.getItemMeta();
        bm.setTitle(title);

        bm.addPage("");
        bm.addPage("page 2");
        bm.addPage("page 3");
        bm.addPage("page 4");

        itemStack.setItemMeta(bm); //set the meta of the book to the above
    }
}