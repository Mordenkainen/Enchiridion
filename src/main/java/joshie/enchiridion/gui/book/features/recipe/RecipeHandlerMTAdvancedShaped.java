package joshie.enchiridion.gui.book.features.recipe;

/*public class RecipeHandlerMTAdvancedShaped extends RecipeHandlerRecipeBase {

    public RecipeHandlerMTAdvancedShaped() {}
    public RecipeHandlerMTAdvancedShaped(IRecipe recipe) {
        try {
            ShapedRecipe mtRecipe = (ShapedRecipe) shaped.get(recipe);
            IIngredient[] inputs = mtRecipe.getIngredients();
            ItemStack outStack = MineTweakerMC.getItemStack(mtRecipe.getOutput());
            ArrayList<Object> objects = new ArrayList();
            for (IIngredient ingredient: inputs) {
                if (ingredient instanceof IItemStack) objects.add(MineTweakerMC.getItemStack(ingredient));
                if (ingredient instanceof IOreDictEntry) objects.add(((IOreDictEntry) ingredient).getName());
            }
            
            init(outStack, objects, mtRecipe.getWidth());
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @Override
    public String getRecipeName() {
        return "MTAdvancedShapedRecipe";
    }
    
    @Override
    protected Class getHandlerClass() {
        return this.getClass();
    }
    
    @Override
    protected Class getRecipeClass() {
        return ShapedRecipeAdvanced.class;
    }

    private static Field shaped;
    static {
        try {
            shaped = ShapedRecipeAdvanced.class.getDeclaredField("recipe");
            shaped.setAccessible(true);
        } catch (Exception e) {}

}}*/
