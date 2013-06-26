package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.ConfigIpFilter;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class IpListTest {

    @Test
    public void getIpCounts() throws IOException {
        List<String> ipConfList = IpListUtil.loadConfigIpPatternList();
        long ipCounts = 0;
        for (String ipConf : ipConfList) {
            if (ipConf.matches("[0-9]+(\\.)(\\*)"))
                ipCounts += 256 * 256 * 256;
            else if (ipConf.matches("[0-9]+(\\.)[0-9]+(\\.)(\\*)"))
                ipCounts += 256 * 256;
            else if (ipConf.matches("[0-9]+(\\.)[0-9]+(\\.)[0-9]+(\\.)(\\*)"))
                ipCounts += 256;
            else
                ipCounts += 1;
        }
        System.out.print("ip count = " + ipCounts);
    }

    @Ignore
    @Test
    public void shouldDenyAllIpInIplistFile() throws IOException {
        ConfigIpFilter filter = IpListUtil.createConfigIpFilterUsingIpList();

        long count = 0;
        long before = System.currentTimeMillis();

        List<IpIterator> ipIterList = IpListUtil.allIpIteratorList();
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
}
