package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.ConfigIpFilter;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigIpFilterPerfTest {

    @Test
    public void shouldExecuteAcceptMethodQuickly() throws IOException {
        ConfigIpFilter filter = IpListUtil.createConfigIpFilterUsingIpList();

        long count = 0, sum = 0;
        long before, after;

        List<IpIterator> ipIterList = IpListUtil.randomIpIteratorList(100);
        for (IpIterator ipIter : ipIterList) {
            while (ipIter.hasNext()) {
                String ip = ipIter.next();
                before = System.nanoTime();
                filter.accept(ip);
                after = System.nanoTime();
                sum += after - before;
                count++;
                if (count % 10000 == 0)
                    System.out.println(count);
            }
        }
        double average = (double) sum / (double) count / 1000000;
        System.out.println(average);
        assertTrue(average + " < 0.01", average < 0.01);
    }

}
