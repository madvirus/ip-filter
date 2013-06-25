package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.ConfigIpFilter;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigIpFilterPerfTest {

    private static ConfigIpFilter filter;

    @BeforeClass
    public static void initIpFilter() throws IOException {
        List<String> ipConfList = IpConfigUtil.loadConfigIpPatternList();
        System.out.println("all ip config pattern count = " + ipConfList.size());
        Config config = new Config();
        for (String ipConf : ipConfList)
            config.deny(ipConf);
        config.setAllowFirst(true);
        config.setDefaultAllow(true);

        filter = new ConfigIpFilter(config);
    }

    @Test
    public void shouldDenyAllIpInIplistFile() throws IOException {
        long count = 0;
        long before = System.currentTimeMillis();
        List<IpIterator> ipIterList = IpConfigUtil.ipIteratorList();
        for (IpIterator ipIter : ipIterList)
            while (ipIter.hasNext()) {
                String ip = ipIter.next();
                boolean result = filter.accept(ip);
                count++;
                assertFalse(ip, result);

                if (count % 10000000 == 0)
                    System.out.println(count / 10000);
            }
        long after = System.currentTimeMillis();
        System.out.println("total ip count = " + count);
        System.out.println("total running time  = " + (after - before) / 1000);
    }

    @Test
    public void shouldExecuteAcceptMethodQuickly() throws IOException {
        long count = 0, sum = 0;
        long before, after;

        List<IpIterator> ipIterList = IpConfigUtil.ipIteratorList();
        outer:
        for (IpIterator ipIter : ipIterList) {
            while (ipIter.hasNext()) {
                String ip = ipIter.next();
                before = System.nanoTime();
                boolean result = filter.accept(ip);
                after = System.nanoTime();
                sum += after - before;
                count++;
                assertFalse(ip, result);

                if (count == 1000000)
                    break outer;
            }
        }
        double average = (double) sum / (double) count / 1000000;
        System.out.println(average);
        assertTrue(average + " < 0.01", average < 0.01);
    }

}
