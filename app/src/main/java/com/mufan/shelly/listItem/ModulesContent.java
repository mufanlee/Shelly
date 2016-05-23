package com.mufan.shelly.listItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing module content for Modules List
 */
public class ModulesContent {

    /**
     * An array of module items.
     */
    public static final List<Module> ITEMS = new ArrayList<Module>();

    public static void addItem(Module item) {
        ITEMS.add(item);
    }

    /**
     * A module item representing a module String.
     */
    public static class Module {
        public final String id;
        public final String content;
        public final String details;

        public Module(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
