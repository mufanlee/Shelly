package com.mufan.shelly.listItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing host content for HostFragment
 */
public class HostContent {

    /**
     * An array of host items.
     */
    public static final List<HostItem> ITEMS = new ArrayList<HostItem>();

    /**
     * A map of host items, by ID.
     */
    public static final Map<String, HostItem> ITEM_MAP = new HashMap<String, HostItem>();

    public static void addItem(HostItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.mac_addr, item);
    }

    /**
     * A host item representing a host.
     */
    public static class HostItem {
        public final String mac_addr;
        public final String ipv4_addr;
        public final int vlan;
        public final String attach_sw;
        public final int attach_port;
        public final Date lastSeen;

        public HostItem(String mac_addr, String ipv4_addr, int vlan, String attach_sw, int attach_port, Date lastSeen) {
            this.mac_addr = mac_addr;
            this.ipv4_addr = ipv4_addr;
            this.vlan = vlan;
            this.attach_sw = attach_sw;
            this.attach_port = attach_port;
            this.lastSeen = lastSeen;
        }
    }
}
