package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.ConfigIpFilter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PerfMeasureConfigIpFilterTest {

    @Test
    public void runPerformanceMeasurement() throws IOException {
        ConfigIpFilter filter = IpListUtil.createConfigIpFilterUsingIpList();
        MeasurementData data = new IpFilterPerfExecutor(filter).execute();
        data.printReport();
    }

}
