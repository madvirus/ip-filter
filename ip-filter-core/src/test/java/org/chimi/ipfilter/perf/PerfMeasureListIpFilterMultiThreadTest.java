package org.chimi.ipfilter.perf;

import org.junit.Test;

import java.io.IOException;

public class PerfMeasureListIpFilterMultiThreadTest {

    @Test
    public void runPerformanceMeasurement() throws IOException {
        ListIpFilter filter = IpListUtil.createListIpFilterUsingIpList();
        MeasurementData data = new IpFilterPerfExecutor(filter).executeInMultithread(10);
        data.printReport();
    }

}
