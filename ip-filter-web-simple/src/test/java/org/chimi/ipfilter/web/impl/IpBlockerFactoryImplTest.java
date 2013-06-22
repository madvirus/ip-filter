package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.web.IpBlocker;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IpBlockerFactoryImplTest {
    @Test
    public void shouldCreateIpBlockerImplInstance() {
        IpBlocker ipBlocker = new IpBlockerFactoryImpl().create(ConfigMapUtil.getTextConfigMap());

        assertTrue(ipBlocker instanceof IpBlockerImpl);
    }
}
