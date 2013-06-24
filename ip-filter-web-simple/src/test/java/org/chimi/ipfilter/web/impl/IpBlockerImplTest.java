package org.chimi.ipfilter.web.impl;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IpBlockerImplTest {

    @Test
    public void configText() {
        IpBlockerImpl ipBlocker = new IpBlockerImpl();
        ipBlocker.init(ConfigMapUtil.getTextConfigMap());

        assertTrue(ipBlocker.accept(ConfigMapUtil.ALLOW_IP));
        assertFalse(ipBlocker.accept(ConfigMapUtil.DENY_IP));
    }

    @Test
    public void configFile() {
        Map<String, String> config = new HashMap<String, String>();
        config.put("type", "file");
        config.put("value",
                "ip-filter-web-simple/src/test/resources/test.conf");
        IpBlockerImpl ipBlocker = new IpBlockerImpl();
        ipBlocker.init(config);

        assertTrue(ipBlocker.accept(ConfigMapUtil.ALLOW_IP));
        assertFalse(ipBlocker.accept(ConfigMapUtil.DENY_IP));
    }

    @Test
    public void configClasspath() {
        Map<String, String> config = new HashMap<String, String>();
        config.put("type", "classpath");
        config.put("value",
                "/test.conf");
        IpBlockerImpl ipBlocker = new IpBlockerImpl();
        ipBlocker.init(config);

        assertTrue(ipBlocker.accept(ConfigMapUtil.ALLOW_IP));
        assertFalse(ipBlocker.accept(ConfigMapUtil.DENY_IP));
    }
}
