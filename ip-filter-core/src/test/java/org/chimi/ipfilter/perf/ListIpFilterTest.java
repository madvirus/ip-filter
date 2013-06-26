package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.ConfigIpFilter;
import org.chimi.ipfilter.IpFilter;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ListIpFilterTest {
    @Test
    public void listIpFilter() {
        Config config = new Config();
        config.setDefaultAllow(true);
        config.setAllowFirst(true);
        config.allow("1.2.3.4");
        config.allow("1.2.3.5");
        config.allow("1.2.3.64/26"); // // 01xxxxxx 범위 : 64~127
        config.deny("5.6.7.8");
        config.deny("101.102.103.32/27"); // 001xxxxx 범위: 32~63")

        IpFilter ipFilter = new ListIpFilter(config);
        assertTrue(ipFilter.accept("1.2.3.4"));
        assertTrue(ipFilter.accept("1.2.3.5"));
        assertTrue(ipFilter.accept("1.2.3.64"));
        assertTrue(ipFilter.accept("1.2.3.65"));
        assertTrue(ipFilter.accept("1.2.3.127"));

    }
}
