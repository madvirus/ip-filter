package org.chimi.ipfilter;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigIpFilterTest {
    @Test
    public void shouldReturnTrueToAllowedIpAndReturnFalseToDeniedIp() {
        Config config = new Config();
        config.setDefaultAllow(true);
        config.setAllowFirst(true);
        config.allow("1.2.3.4");
        config.allow("1.2.3.5");
        config.allow("1.2.3.64/26"); // // 01xxxxxx 범위 : 64~127
        config.deny("5.6.7.8");
        config.deny("101.102.103.32/27"); // 001xxxxx 범위: 32~63")

        IpFilter ipFilter = new ConfigIpFilter(config);
        assertTrue(ipFilter.accept("1.2.3.4"));
        assertTrue(ipFilter.accept("1.2.3.5"));
        assertTrue(ipFilter.accept("1.2.3.64"));
        assertTrue(ipFilter.accept("1.2.3.65"));
        assertTrue(ipFilter.accept("1.2.3.127"));
    }

    @Test
    public void shouldReturnTrueDupIpInAllowAndDenyListWhenAllowFirst() {
        Config config = new Config();
        config.setAllowFirst(true);
        config.allow("1.2.3.4");
        config.deny("1.2.3.4");
        IpFilter ipFilter = new ConfigIpFilter(config);
        assertTrue(ipFilter.accept("1.2.3.4"));
    }

    @Test
    public void shouldReturnFalseDupIpInAllowAndDenyListWhenDenyFirst() {
        Config config = new Config();
        config.setAllowFirst(false);
        config.allow("1.2.3.4");
        config.deny("1.2.3.4");
        IpFilter ipFilter = new ConfigIpFilter(config);
        assertFalse(ipFilter.accept("1.2.3.4"));
    }
}
