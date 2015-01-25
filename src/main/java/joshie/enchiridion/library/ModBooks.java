package joshie.enchiridion.library;

import static joshie.enchiridion.api.EnchiridionHelper.bookRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.FileUtils;

import com.google.gson.annotations.Expose;

import cpw.mods.fml.common.Loader;

public class ModBooks {
    @Expose
    private ArrayList<BookData> books = new ArrayList();

    public ModBooks addBook(BookData book) {
        books.add(book);
        return this;
    }

    public static class BookData {
        @Expose
        public String mod;
        @Expose
        public String stack;
        @Expose
        public String type;

        public BookData() {}

        public BookData(String mod, String item, int meta, String register) {
            this.mod = mod;
            this.stack = mod + ":" + item + " " + meta;
            this.type = register;
        }

        public BookData(String mod, ItemStack stack, String register) {
            this.mod = mod;
            this.stack = StackHelper.getStringFromStack(stack);
            this.type = register;
        }
    }

    public static void init() {
        try {
            ModBooks data = null;
            File default_file = new File(Enchiridion.root, "library/default.json");
            if (!default_file.exists()) {
                data = getModBooks(new ModBooks());

                File parent = default_file.getParentFile();
                if (!parent.exists() && !parent.mkdirs()) {
                    throw new IllegalStateException("Couldn't create dir: " + parent);
                }

                Writer writer = new OutputStreamWriter(new FileOutputStream(default_file), "UTF-8");
                writer.write(WikiHelper.getGson().toJson(data));
                writer.close();
            } else {
                String json = FileUtils.readFileToString(default_file);
                data = WikiHelper.getGson().fromJson(json, ModBooks.class);
            }

            //Now that we have the book data let's go through and register them
            for (BookData book : data.books) {
                if (Loader.isModLoaded(book.mod)) {
                    ItemStack item = StackHelper.getStackFromString(book.stack);
                    if (item != null && item.getItem() != null) {
                        if (book.type.equals("default")) {
                            bookRegistry.registerDefault(item);
                        } else if (book.type.equals("network")) {
                            bookRegistry.registerNetworkSwitch(item);
                        } else if (book.type.equals("switch")) {
                            bookRegistry.registerSwitch(item);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Default books in the json file **/
    public static ModBooks getModBooks(ModBooks data) {
        data.addBook(new BookData("Botania", "lexicon", 0, "default"));
        data.addBook(new BookData("HardcoreQuesting", "quest_book", 0, "network"));
        data.addBook(new BookData("factorization", "docbook", 0, "default"));
        data.addBook(new BookData("Mariculture", "guide", 0, "switch"));
        data.addBook(new BookData("Mariculture", "guide", 1, "switch"));
        data.addBook(new BookData("Mariculture", "guide", 2, "switch"));
        data.addBook(new BookData("Mariculture", "guide", 3, "switch"));
        data.addBook(new BookData("Mariculture", "guide", 4, "switch"));
        data.addBook(new BookData("OpenBlocks", "infoBook", 0, "default"));
        data.addBook(new BookData("TConstruct", "manualBook", 0, "default"));
        data.addBook(new BookData("TConstruct", "manualBook", 1, "default"));
        data.addBook(new BookData("TConstruct", "manualBook", 2, "default"));
        data.addBook(new BookData("TConstruct", "manualBook", 3, "default"));
        data.addBook(new BookData("Thaumcraft", "ItemThaumonomicon", 0, "default"));
        data.addBook(new BookData("witchery", "ingredient", 46, "switch"));
        data.addBook(new BookData("witchery", "ingredient", 47, "switch"));
        data.addBook(new BookData("witchery", "ingredient", 48, "switch"));
        data.addBook(new BookData("witchery", "ingredient", 49, "switch"));
        data.addBook(new BookData("witchery", "ingredient", 81, "switch"));
        data.addBook(new BookData("witchery", "ingredient", 106, "switch"));
        data.addBook(new BookData("witchery", "ingredient", 107, "switch"));
        data.addBook(new BookData("witchery", "ingredient", 127, "switch"));
        return data;
    }
}
