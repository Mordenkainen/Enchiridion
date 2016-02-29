package joshie.enchiridion.books.buttons;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.features.FeatureItem;

public class ButtonInsertItem extends AbstractButton {
	public ButtonInsertItem() {
		super("item");
	}

	@Override
	public void performAction() {
		IPage current = EnchiridionAPI.book.getPage();
		FeatureItem feature = new FeatureItem(EConfig.getDefaultItem());
		current.addFeature(feature, 0, 0, 16D, 16D, false, false);
	}
}