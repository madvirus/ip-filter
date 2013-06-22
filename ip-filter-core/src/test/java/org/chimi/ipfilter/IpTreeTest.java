package org.chimi.ipfilter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IpTreeTest {

    private IpTree ipTree;

    @Before
    public void setUp() throws Exception {
        ipTree = new IpTree();
    }

    @Test
    public void simpleIpTree() {
        ipTree.add("1.2.3.4");
        ipTree.add("1.2.3.5");
        assertTrue(ipTree.containsIp("1.2.3.4"));
        assertTrue(ipTree.containsIp("1.2.3.5"));
        assertFalse(ipTree.containsIp("1.2.3.6"));
        assertFalse(ipTree.containsIp("2.3.4.5"));
    }

    @Test
    public void rangePatternIpTree() {
        ipTree.add("1.2.3.64/26"); // 01xxxxxx 범위 : 64~127
        ipTree.add("101.102.103.32/27"); // 001xxxxx 범위: 32~63
        ipTree.add("10.20.30.0/24"); // xxxxxxxx 범위: 0~255

        assertIpTreeContainsIp(ipTree, "1.2.3", 64, 127, true);
        assertIpTreeContainsIp(ipTree, "1.2.3", 1, 63, false);
        assertIpTreeContainsIp(ipTree, "1.2.3", 128, 255, false);

        assertIpTreeContainsIp(ipTree, "101.102.103", 32, 63, true);
        assertIpTreeContainsIp(ipTree, "101.102.103", 1, 31, false);
        assertIpTreeContainsIp(ipTree, "101.102.103", 64, 255, false);

        assertIpTreeContainsIp(ipTree, "10.20.30", 0, 255, true);

        assertFalse(ipTree.containsIp("2.3.4.128"));
    }

    private void assertIpTreeContainsIp(IpTree ipTree, String prefixIp, int from, int to, boolean expected) {
        for (int i = from ; i <= to ; i++) {
            String ip = prefixIp + "." + i;
            assertEquals(ip, expected, ipTree.containsIp(ip));
        }
    }

    @Test
    public void level4StarIpTree() {
        ipTree.add("10.20.30.*");

        assertIpTreeContainsIp(ipTree, "10.20.30", 0, 255, true);
    }

}
