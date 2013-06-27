package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.IpFilter;
import org.chimi.ipfilter.IpFilterTestUtil;
import org.junit.Test;

public class ListIpFilterTest {

    @Test
    public void shouldReturnTrueToAllowedIp() {
        Config config = IpFilterTestUtil.createConfigForAllow();
        IpFilter ipFilter = new ListIpFilter(config);
        IpFilterTestUtil.assertAcceptForAllow(ipFilter);
    }

    @Test
    public void shouldReturnFalseToDeniedIp() {
        Config config = IpFilterTestUtil.createConfigForDeny();
        IpFilter ipFilter = new ListIpFilter(config);
        IpFilterTestUtil.assertAcceptForDeny(ipFilter);
    }

}
