package org.chimi.ipfilter;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IpFiltersTest {
    @Test
    public void createIpFilterWithConfig() {
        IpFilter filter = IpFilters.create(createConfig());
        assertFalse(filter instanceof CachedIpFilter);
        assertTrue(filter instanceof ConfigIpFilter);
    }

    @Test
    public void createCachedIpFilterWithConfig() {
        IpFilter filter = IpFilters.createCached(createConfig());
        assertFalse(filter instanceof ConfigIpFilter);
        assertTrue(filter instanceof CachedIpFilter);
    }

    private Config createConfig() {
        Config config = new Config();
        config.allow("1.2.3.4");
        return config;
    }
}
