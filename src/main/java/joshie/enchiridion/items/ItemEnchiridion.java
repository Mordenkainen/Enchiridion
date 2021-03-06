package joshie.enchiridion.items;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.DARK_GREEN;
import static net.minecraft.util.text.TextFormatting.RESET;

public class ItemEnchiridion extends Item {
    public ItemEnchiridion() {
        setHasSubtypes(true);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stack.getItemDamage() == 1 ? 1 : 16;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getItemDamage() == 1) {
            return TextFormatting.GOLD + Enchiridion.translate("library");
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return Enchiridion.format("new", DARK_GREEN, RESET);
        }

        IBook book = BookRegistry.INSTANCE.getBook(stack);
        return book == null ? Enchiridion.format("new", DARK_GREEN, RESET) : book.getDisplayName();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.getItemDamage() == 1) {
            int currentBook = LibraryHelper.getLibraryContents(playerIn).getCurrentBook();
            ItemStack internal = LibraryHelper.getLibraryContents(playerIn).getStackInSlot(currentBook);
            if (internal != null) {
                tooltip.addAll(internal.getTooltip(playerIn, advanced));
            }
        } else {
            IBook book = BookRegistry.INSTANCE.getBook(stack);
            if (book != null) {
                book.addInformation(tooltip);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (stack.getItemDamage() == 1) {
            if (player.isSneaking()) player.openGui(Enchiridion.instance, GuiIDs.LIBRARY, world, 0, hand.ordinal(), 0);
            else {
                int currentBook = LibraryHelper.getLibraryContents(player).getCurrentBook();
                ItemStack book = LibraryHelper.getLibraryContents(player).getStackInSlot(currentBook);
                if (book != null) {
                    IBookHandler handler = EnchiridionAPI.library.getBookHandlerForStack(book);
                    if (handler != null) {
                        handler.handle(book, player, hand, currentBook, player.isSneaking());
                    }
                } else player.openGui(Enchiridion.instance, GuiIDs.LIBRARY, world, 0, hand.ordinal(), 0);
            }

        } else player.openGui(Enchiridion.instance, GuiIDs.BOOK, world, 0, hand.ordinal(), 0);
        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        if (EConfig.libraryAsItem) list.add(new ItemStack(item, 1, 1));

        list.add(new ItemStack(item));

        for (String book : BookRegistry.INSTANCE.getUniqueNames()) {
            ItemStack stack = new ItemStack(item);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("identifier", book);
            list.add(stack);
        }
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        super.setUnlocalizedName(unlocalizedName);
        setRegistryName(unlocalizedName);
        GameRegistry.register(this);
        return this;
    }
}
