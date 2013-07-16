package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.ConfigIpFilter;
import org.junit.Test;

import java.io.IOException;

public class PerfMeasureConfigIpFilterMultiThreadTest {

    @Test
    public void runPerformanceMeasurement() throws IOException {
        ConfigIpFilter filter = IpListUtil.createConfigIpFilterUsingIpList();
        MeasurementData data = new IpFilterPerfExecutor(filter).executeInMultithread(20);
        data.printReport();
    }

}
