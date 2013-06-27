package org.chimi.ipfilter;

import static org.junit.Assert.assertEquals;

public class IpFilterTestUtil {
    private static String[] ipPatterns = {
            "1.2.3.4",
            "1.2.3.5",
            "1.2.3.64/26",
            "10.20.*",
            "10.10.10.*"
    };

    public static Config createConfigForAllow() {
        Config config = new Config();
        config.setDefaultAllow(false);
        config.setAllowFirst(true);
        for (String ipPattern : ipPatterns)
            config.allow(ipPattern);
        return config;
    }

    public static void assertAcceptForAllow(IpFilter ipFilter) {
        assertAccept(ipFilter, true);
    }

    private static void assertAccept(IpFilter ipFilter, boolean b) {
        assertEquals(b, ipFilter.accept("1.2.3.4"));
        assertEquals(b, ipFilter.accept("1.2.3.5"));
        assertEquals(b, ipFilter.accept("1.2.3.64"));
        assertEquals(b, ipFilter.accept("1.2.3.65"));
        assertEquals(b, ipFilter.accept("1.2.3.127"));
        assertEquals(b, ipFilter.accept("10.10.10.1"));
        assertEquals(b, ipFilter.accept("10.20.1.1"));
        assertEquals(b, ipFilter.accept("10.20.10.10"));
        assertEquals(!b, ipFilter.accept("10.10.11.1"));
        assertEquals(!b, ipFilter.accept("1.2.3.63"));
    }

    public static Config createConfigForDeny() {
        Config config = new Config();
        config.setDefaultAllow(true);
        config.setAllowFirst(true);
        for (String ipPattern : ipPatterns)
            config.deny(ipPattern);
        return config;
    }

    public static void assertAcceptForDeny(IpFilter ipFilter) {
        assertAccept(ipFilter, false);
    }
}
