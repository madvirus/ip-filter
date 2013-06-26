package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.ConfigIpFilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IpListUtil {

    public static ConfigIpFilter createConfigIpFilterUsingIpList() throws IOException {
        List<String> ipConfList = IpListUtil.loadConfigIpPatternList();
        Config config = new Config();
        for (String ipConf : ipConfList)
            config.deny(ipConf);
        config.setAllowFirst(true);
        config.setDefaultAllow(true);
        return new ConfigIpFilter(config);
    }

    public static List<IpIterator> randomIpIteratorList(int count) throws IOException {
        final List<String[]> ipRangeList = new ArrayList<String[]>(40000);
        template(new StringReceiver() {
            @Override
            public void receive(String line) {
                String[] ipRange = line.split("\t");
                ipRangeList.add(ipRange);
            }
        });

        final List<IpIterator> ipIteratorList = new ArrayList<IpIterator>();
        Random random = new Random();
        int maxValue = ipRangeList.size();
        for (int i = 1 ; i <= count ; i++) {
            String[] ipRange = ipRangeList.get(random.nextInt(maxValue));
            ipIteratorList.add(new IpIterator(ipRange[0], ipRange[1]));
        }
        return ipIteratorList;
    }

    public static List<IpIterator> allIpIteratorList() throws IOException {
        final List<IpIterator> ipIteratorList = new ArrayList<IpIterator>();
        template(new StringReceiver() {
            @Override
            public void receive(String line) {
                String[] ipRange = line.split("\t");
                ipIteratorList.add(new IpIterator(ipRange[0], ipRange[1]));
            }
        });
        return ipIteratorList;
    }

    public static List<String> loadConfigIpPatternList() throws IOException {
        final List<String> allIpPatternList = new ArrayList<String>(2048);
        final IpRangeParser parser = new IpRangeParser();

        template(new StringReceiver() {
            @Override
            public void receive(String line) {
                List<String> ipList = parser.parse(line);
                allIpPatternList.addAll(ipList);
            }
        });
        return allIpPatternList;
    }

    private static void template(StringReceiver receiver) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("ip-filter-core/src/test/resources/org/chimi/ipfilter/perf/iplist.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                receiver.receive(line);
            }
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    public static interface StringReceiver {
        void receive(String line);
    }
}
