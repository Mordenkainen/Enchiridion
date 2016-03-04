package joshie.enchiridion.library;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.SyncHelper;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketOpenLibrary;
import joshie.enchiridion.network.PacketSyncLibraryAllowed;
import joshie.enchiridion.network.PacketSyncLibraryContents;
import joshie.enchiridion.network.PacketSyncMD5;
import joshie.lib.helpers.ClientHelper;
import joshie.lib.helpers.MCServerHelper;
import joshie.lib.util.PacketPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LibraryEvents {
    //Setup the Client
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (event.gui instanceof GuiSelectWorld || event.gui instanceof GuiMultiplayer) {
            LibraryHelper.resetClient();
        }
    }

    //Sync the library
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) { //Sync what's in the library
            EntityPlayerMP mp = (EntityPlayerMP) player;
            if (!SyncHelper.playersSynced.contains(mp)) {
                if (EConfig.debugMode) Enchiridion.log(Level.INFO, "Did you call me?");
                PacketHandler.sendToClient(new PacketSyncLibraryContents(LibraryHelper.getServerLibraryContents(player)), mp);
                //Sync what's allowed in the library
                String serverName = MCServerHelper.getHostName();
                PacketHandler.sendToClient(new PacketSyncLibraryAllowed(PacketPart.SEND_HASH, serverName, ModSupport.getHashcode(serverName)), mp);
    
                //Start the md5 process
                if (EConfig.syncDataAndImagesToClients) {
                    PacketHandler.sendToClient(new PacketSyncMD5(PacketPart.SEND_SIZE, "", SyncHelper.servermd5.length), mp);
                }
                
                SyncHelper.playersSynced.add(mp);
            }
        }
    }

    //Opening the key binding
    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event) {
        if (GameSettings.isKeyDown(EClientProxy.libraryKeyBinding) && Minecraft.getMinecraft().inGameHasFocus && !Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            PacketHandler.sendToServer(new PacketOpenLibrary()); //Let the server know!
            ClientHelper.getPlayer().openGui(Enchiridion.instance, GuiIDs.LIBRARY, ClientHelper.getWorld(), 0, 0, 0);
        }
    }
}
