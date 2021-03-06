package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class RightClickBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "rightclick";
    }

    @Override
    public void handle(ItemStack stack, EntityPlayer player, EnumHand hand, int slotID, boolean isShiftPressed) {
        try {
            ItemStack ret = stack.useItemRightClick(player.worldObj, player, hand).getResult();
            EnchiridionAPI.library.getLibraryInventory(player).setInventorySlotContents(slotID, ret);
        } catch (Exception e) {}
    }
}
