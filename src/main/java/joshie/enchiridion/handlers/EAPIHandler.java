package joshie.enchiridion.handlers;

import java.io.File;

import org.apache.logging.log4j.Level;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.IEnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.api.gui.IToolbarButton;
import joshie.enchiridion.api.recipe.IRecipeHandler;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.gui.book.GuiSimpleEditorButton;
import joshie.enchiridion.gui.book.GuiToolbar;
import joshie.enchiridion.gui.book.features.FeatureRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class EAPIHandler implements IEnchiridionAPI {
    @Override
    public void registerModWithBooks(String id) {
        /** Grab the modid and the assets path **/
        String modid = id;
        String assetspath = id.toLowerCase();
        if (id.contains(":")) {
            String[] split = id.split(":");
            modid = split[0];
            assetspath = split[1].toLowerCase();
        }
        
        /** Find this mods container **/
        ModContainer mod = null;
        for (ModContainer container: Loader.instance().getActiveModList()) {
            if (container.getModId().equals(modid)) {
                mod = container;
                break;
            }
        }
        
        /** Attempt to register in dev or in jar **/
        if (mod == null) {
            Enchiridion.log(Level.ERROR, "When attempting to register books with Enchiridion a mod with the modid " + modid + " could not be found");
        } else {
            String jar = mod.getSource().toString();
            if (jar.contains(".jar") || jar.contains(".zip")) {
                BookRegistry.INSTANCE.registerModInJar(assetspath, new File(jar));
            } else {
                BookRegistry.INSTANCE.registerModInDev(assetspath, mod.getSource());
            }
        }
    }
    
    @Override
    public void registerBookHandler(IBookHandler handler) {
        //BookHandlerRegistry.registerHandler(handler);
    }
	
	@Override
	public void registerEditorOverlay(IBookEditorOverlay overlay) {
		GuiBook.INSTANCE.registerOverlay(overlay);
	}

	@Override
	public void registerToolbarButton(IToolbarButton button) {
		GuiToolbar.INSTANCE.registerButton(button);
	}

    @Override
    public void registerRecipeHandler(IRecipeHandler handler) {
        FeatureRecipe.handlers.add(handler);
        Enchiridion.log(Level.INFO, "Registered a new recipe handler: " + handler.getRecipeName());
    }

	@Override
	public void registerButtonAction(IButtonAction action) {
		GuiSimpleEditorButton.INSTANCE.registerAction(action);
	}
}
