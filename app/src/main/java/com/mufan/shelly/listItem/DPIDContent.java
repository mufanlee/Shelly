package com.mufan.shelly.listItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing DPID content for DPIDItemFragment
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DPIDContent {

    /**
     * An array of DPID items.
     */
    public static final List<DPIDItem> ITEMS = new ArrayList<DPIDItem>();

    public static void addItem(DPIDItem item) {
        ITEMS.add(item);
    }

    /**
     * A DPID item representing a switch DPID.
     */
    public static class DPIDItem {
        public final String id;
        public final String content;
        public final String details;

        public DPIDItem(String id, String content, String details) {
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
