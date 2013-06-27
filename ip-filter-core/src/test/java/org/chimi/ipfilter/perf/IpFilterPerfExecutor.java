package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.IpFilter;

import java.io.IOException;
import java.util.List;

class IpFilterPerfExecutor {
    private IpFilter filter;

    public IpFilterPerfExecutor(IpFilter filter) {
        this.filter = filter;
    }

    public MeasurementData execute() throws IOException {
        MeasurementData sm = new MeasurementData();

        List<IpIterator> ipIterList = IpListUtil.randomIpIteratorList(5);
        for (IpIterator ipIter : ipIterList) {
            while (ipIter.hasNext()) {
                String ip = ipIter.next();
                sm.start();
                filter.accept(ip);
                sm.stop();

                if (sm.getCount() % 100000 == 0)
                    System.out.println(sm.getCount());
            }
        }
        return sm;
    }
}
