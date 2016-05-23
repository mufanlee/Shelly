package com.mufan.shelly.listItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing table content for Table list
 */
public class TablesContent {

    /**
     * An array of table items.
     */
    public static final List<DBTable> ITEMS = new ArrayList<>();

    public static void addItem(DBTable item) {
        ITEMS.add(item);
    }

    /**
     * DBTable in the list, which is the table name from Controller.
     */
    public static class DBTable {
        public final String id;
        public final String content;
        public final String details;

        public DBTable(String id, String content, String details) {
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
