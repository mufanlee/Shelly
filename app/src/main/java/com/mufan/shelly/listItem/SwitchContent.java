package com.mufan.shelly.listItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing switch content for SwitchItemFragment
 */
public class SwitchContent {

    /**
     * An array of swtich items.
     */
    public static final List<SwitchItem> ITEMS = new ArrayList<SwitchItem>();

    /**
     * A map of swtich items, by ID.
     */
    public static final Map<String, SwitchItem> ITEM_MAP = new HashMap<String, SwitchItem>();

    public static void addItem(SwitchItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.dpid, item);
    }

    /**
     * A switch item representing a switch.
     */
    public static class SwitchItem {
        public final String dpid;
        public final String mfr_desc;
        public final String hw_desc;
        public final String sw_desc;
        public final String serial_num;
        public final String dp_desc;

        public SwitchItem(String dpid, String mfr_desc, String hw_desc,String sw_desc, String serial_num, String dp_desc) {
            this.dpid = dpid;
            this.mfr_desc = mfr_desc;
            this.sw_desc = sw_desc;
            this.hw_desc = hw_desc;
            this.serial_num = serial_num;
            this.dp_desc = dp_desc;
        }

        @Override
        public String toString() {
            return "SwitchItem{" +
                    "dpid='" + dpid + '\'' +
                    ", mfr_desc='" + mfr_desc + '\'' +
                    ", hw_desc='" + hw_desc + '\'' +
                    ", sw_desc='" + sw_desc + '\'' +
                    ", serial_num='" + serial_num + '\'' +
                    ", dp_desc='" + dp_desc + '\'' +
                    '}';
        }
    }
}
