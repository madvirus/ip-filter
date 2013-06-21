package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.web.IpBlocker;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class IpBlockerFactoryImplTest {
    @Test
    public void shouldCreateIpBlockerImplInstance() {
        Map<String, String> config = new HashMap<String, String>();
        IpBlocker ipBlocker = new IpBlockerFactoryImpl().create(config);

        assertTrue(ipBlocker instanceof IpBlockerImpl);
    }
}
