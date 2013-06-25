package org.chimi.ipfilter.perf;

import java.util.ArrayList;
import java.util.List;

public class IpRangeParser {

    private int from1;
    private int from2;
    private int from3;
    private int from4;
    private int to1;
    private int to2;
    private int to3;
    private int to4;
    private List<String> ipPatternList;

    public List<String> parse(String ipRangeString) {
        replaceIpData(ipRangeString);
        ipPatternList = new ArrayList<String>();

        if (sameAllLevel()) {
            ipPatternList.add(from1 + "." + from2 + "." + from3 + "." + from4);
            return ipPatternList;
        }
        if (sameToLevel3()) {
            return getIpPatternListWhenSameToLevel3();
        }
        if (sameToLevel2()) {
            return getIpPatternListWhenSameToLevel2();
        }
        if (sameToLevel1()) {
            return getIpPatternListWhenSameToLevel1();
        }
        return getIpPatternListWhenNoSame();
    }

    private List<String> getIpPatternListWhenNoSame() {
        if (from1 == to1 - 1 && from2 > 0 && from3 > 0 && from4 == 0 &&
                to2 > 0 && to3_255() && to4_255()) {
            // ex: 59.191.240.0 ~ 60.31.255.255
            for (int i = from3; i <= 255; i++) // 59.191.240.* ~ 59.191.255.*
                ipPatternList.add(from1 + "." + from2 + "." + i + ".*");
            for (int i = from2 + 1; i <= 255; i++) // 59.192.* ~ 59.255.*
                ipPatternList.add(from1 + "." + i + ".*");
            for (int i = 0; i <= to2; i++) // 60.0.* ~ 60.31.*
                ipPatternList.add(to1 + "." + i + ".*");
            return ipPatternList;
        }
        if (from1 == to1 - 1 && from2 > 0 && from3 == 0 && from4 == 0 &&
                to2 > 0 && to3_255() && to4_255()) {
            // ex: 110.240.0.0 ~ 111.63.255.255
            for (int i = from2; i <= 255; i++) // 110.240.* ~ 110.255.*
                ipPatternList.add(from1 + "." + i + ".*");
            for (int i = 0; i <= to2; i++) // 111.0.* ~ 111.63.*
                ipPatternList.add(to1 + "." + i + ".*");
            return ipPatternList;
        }

        if (from1 == to1 - 1 && from2 > 0 && from3 == 0 && from4 == 0 &&
                to2 > 0 && to3 < 255 && to4_255()) {
            // ex: 118.244.0.0 ~ 119.2.31.255
            for (int i = from2; i <= 255; i++) // 118.244.* ~ 118.255.*
                ipPatternList.add(from1 + "." + i + ".*");
            for (int i = 0; i < to2; i++) // 119.0.* ~ 119.1.*
                ipPatternList.add(to1 + "." + i + ".*");
            for (int i = 0; i <= to3; i++) // 119.2.0.* ~ 119.2.31.*
                ipPatternList.add(to1 + "." + to2 + "." + i + ".*");
            return ipPatternList;
        }
        return null;
    }

    private boolean to4_255() {
        return to4 == 255;
    }

    private boolean to3_255() {
        return to3 == 255;
    }

    private List<String> getIpPatternListWhenSameToLevel1() {
        if (sameToLevel1() && from3 == 0 && from4 == 0 && to3_255() && to4_255()) {
            // a.X.0.0 ~ a.Y.255.255
            for (int i = from2; i <= to2; i++)
                ipPatternList.add(from1 + "." + i + ".*");
            return ipPatternList;
        }
        if (sameToLevel1() && from3 > 0 && from4 == 0 && to3_255() && to4_255()) {
            //a.X1.X2.0 ~ a.Y.255.255
            for (int i = from3; i <= 255; i++)
                ipPatternList.add(from1 + "." + from2 + "." + i + ".*");
            for (int i = from2 + 1; i <= to2; i++)
                ipPatternList.add(from1 + "." + i + ".*");
            return ipPatternList;
        }
        if (sameToLevel1() && from3 == 0 && from4 == 0 && to3 < 255 && to4_255()) {
            //a.X.0.0 ~ a.Y1.Y2.255
            for (int i = from2; i < to2; i++) // a.X.* ~ a.(Y1-1).*
                ipPatternList.add(from1 + "." + i + ".*");
            for (int i = 0; i <= to3; i++) // a.Y1.0.* ~ a.Y1.y2.*
                ipPatternList.add(from1 + "." + to2 + "." + i + ".*");
            return ipPatternList;
        }
        if (sameToLevel1() && from3 > 0 && from4 == 0 && to3 < 255 && to4_255()) {
            // ex: 42.0.128.0 ~ 42.1.59.255 / 42.156.36.0 ~ 42.187.123.255
            for (int i = from3; i <= 255; i++) // 42.0.128.* ~ 42.0.255.* / 42.156.36.* ~ 42.156.255.*
                ipPatternList.add(from1 + "." + from2 + "." + i + ".*");
            for (int i = from2 + 1; i < to2; i++) // no / 42.157.* ~ 42.186.*
                ipPatternList.add(from1 + "." + i + ".*");
            for (int i = 0; i <= to3; i++) // 42.1.0.* ~ 42.1.59.* / 42.187.0.* ~ 42.187.123.*
                ipPatternList.add(from1 + "." + to2 + "." + i + ".*");
            return ipPatternList;
        }
        if (sameToLevel1() && from3 == 0 && from4 == 0 && to4 < 255) {
            // ex: 118.24.0.0 ~ 118.27.255.254
            for (int i = from2; i < to2; i++)
                ipPatternList.add(from1 + "." + i + ".*"); // 118.24.* ~ 118.26.*
            for (int i = 0; i < to3; i++)
                ipPatternList.add(from1 + "." + to2 + "." + i + ".*"); // 118.27.0.* ~ 118.27.254.*
            for (int i = 0; i <= to4; i++)
                ipPatternList.add(from1 + "." + to2 + "." + to3 + "." + i); // 118.27.255.0 ~ 118.27.255.254
            return ipPatternList;
        }
        return null;
    }

    private List<String> getIpPatternListWhenSameToLevel2() {
        if (sameToLevel2() && from3 == 0 && from4 == 0 && to3_255() && to4_255()) {
            // a.b.0.0 ~ a.b.255.255
            ipPatternList.add(from1 + "." + from2 + ".*");
            return ipPatternList;
        }
        if (sameToLevel2() && from4 == 0 && to4_255()) {
            // a.b.X.0 ~ a.b.Y.255
            for (int i = from3; i <= to3; i++)
                ipPatternList.add(from1 + "." + from2 + "." + i + ".*");
            return ipPatternList;
        }
        if (sameToLevel2() && from3 != 0 && from4 == 0 && to3 != 255 && to4 != 255) {
            // ex: 106.187.32.0 ~ 106.187.34.175
            for (int i = from3; i < to3; i++) // 106.187.32.* ~ 106.187.33.*
                ipPatternList.add(from1 + "." + from2 + "." + i + ".*");
            for (int i = 0; i <= to4; i++)
                ipPatternList.add(from1 + "." + from2 + "." + to3 + "." + i);
            return ipPatternList;
        }
        if (sameToLevel2() && from3 != 0 && from4 != 0 && to3 != 255 && to4_255()) {
            // ex: 106.187.34.177 ~ 106.187.39.255
            for (int i = from4; i <= 255; i++) // 106.187.34.177 ~ 106.187.34.255
                ipPatternList.add(from1 + "." + from2 + "." + from3 + "." + i);
            for (int i = from3 + 1; i <= to3; i++) // 106.187.35.* ~ 106.187.39.*
                ipPatternList.add(from1 + "." + from2 + "." + i + ".*");
            return ipPatternList;
        }
        return null;
    }

    private List<String> getIpPatternListWhenSameToLevel3() {
        if (sameToLevel3() && from4 == 0 && to4_255()) {
            // a.b.c.0 ~ a.b.c.255
            ipPatternList.add(from1 + "." + from2 + "." + from3 + ".*");
            return ipPatternList;
        }
        if (sameToLevel3()) {
            // a.b.c.x ~ a.b.c.y
            for (int i = from4; i <= to4; i++)
                ipPatternList.add(from1 + "." + from2 + "." + from3 + "." + i);
            return ipPatternList;
        }
        return null;
    }

    private void replaceIpData(String ipRangeString) {
        String[] ipRange = ipRangeString.split("\t");
        int[] fromIp = toInt(ipRange[0]);
        int[] toIp = toInt(ipRange[1]);

        from1 = fromIp[0];
        from2 = fromIp[1];
        from3 = fromIp[2];
        from4 = fromIp[3];
        to1 = toIp[0];
        to2 = toIp[1];
        to3 = toIp[2];
        to4 = toIp[3];
    }

    private boolean sameAllLevel() {
        return from1 == to1 && from2 == to2 && from3 == to3 && from4 == to4;
    }

    private boolean sameToLevel1() {
        return from1 == to1 && from2 != to2;
    }

    private boolean sameToLevel2() {
        return from1 == to1 && from2 == to2 && from3 != to3;
    }

    private boolean sameToLevel3() {
        return from1 == to1 && from2 == to2 && from3 == to3 && from4 != to4;
    }

    private int[] toInt(String ipString) {
        int[] values = new int[4];
        String[] ipValues = ipString.split("\\.");
        for (int i = 0; i < 4; i++)
            values[i] = Integer.parseInt(ipValues[i]);
        return values;
    }
}
