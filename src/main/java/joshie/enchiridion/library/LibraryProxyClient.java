package joshie.enchiridion.library;

import joshie.lib.helpers.ClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LibraryProxyClient extends LibraryProxy {
    private LibraryInventory contents;

    public LibraryProxyClient() {
        contents = new LibraryInventory(ClientHelper.getPlayer());
    }

    @Override
    public LibraryInventory getLibraryInventory(EntityPlayer player) {
        return contents;
    }
}