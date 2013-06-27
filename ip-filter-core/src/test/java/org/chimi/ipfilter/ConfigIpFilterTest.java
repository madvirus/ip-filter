package org.chimi.ipfilter;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigIpFilterTest {
    @Test
    public void shouldReturnTrueToAllowedIp() {
        Config config = IpFilterTestUtil.createConfigForAllow();
        IpFilter ipFilter = new ConfigIpFilter(config);
        IpFilterTestUtil.assertAcceptForAllow(ipFilter);
    }

    @Test
    public void shouldReturnFalseToDeniedIp() {
        Config config = IpFilterTestUtil.createConfigForDeny();
        IpFilter ipFilter = new ConfigIpFilter(config);
        IpFilterTestUtil.assertAcceptForDeny(ipFilter);
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
