package org.chimi.ipfilter.perf;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IpIteratorTest {
    @Test
    public void iteratorShouldHaveAllIpsInRange() {
        String[] expected = new String[256 * 3];
        int idx = 0;
        for (int i = 1; i <= 3; i++)
            for (int j = 0; j <= 255; j++)
                expected[idx++] = "1.0." + i + "." + j;

        idx = 0;
        Iterator<String> ipIter = new IpIterator("1.0.1.0", "1.0.3.255");

        assertTrue(ipIter.hasNext());
        do {
            assertEquals(expected[idx++], ipIter.next());
        } while (ipIter.hasNext());
        assertEquals(256 * 3, idx);
    }
}
