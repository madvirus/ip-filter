package org.chimi.ipfilter.perf;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class IpRangeParserTest {
    @Test
    public void parseIpList() {
        IpRangeParser parser = new IpRangeParser();

        List<String> ipList = parser.parse("1.0.1.0\t1.0.3.255");
        assertEquals(3, ipList.size());
        assertEquals("1.0.1.*", ipList.get(0));
        assertEquals("1.0.2.*", ipList.get(1));
        assertEquals("1.0.3.*", ipList.get(2));

        ipList = parser.parse("1.3.0.0\t1.3.255.255");
        assertEquals(1, ipList.size());
        assertEquals("1.3.*", ipList.get(0));

        ipList = parser.parse("1.12.0.0\t1.15.255.255");
        assertEquals(4, ipList.size());
        assertEquals("1.12.*", ipList.get(0));
        assertEquals("1.13.*", ipList.get(1));
        assertEquals("1.14.*", ipList.get(2));
        assertEquals("1.15.*", ipList.get(3));

        ipList = parser.parse("31.220.30.224\t31.220.30.255");
        assertEquals(255 - 224 + 1, ipList.size());
        for (int i = 224; i <= 255; i++)
            assertEquals("31.220.30." + i, ipList.get(i - 224));

        ipList = parser.parse("36.0.8.0\t36.1.255.255");
        assertEquals(255 - 8 + 1 + 1, ipList.size());
        for (int i = 8; i <= 255; i++)
            assertEquals("36.0." + i + ".*", ipList.get(i - 8));
        assertEquals("36.1.*", ipList.get(ipList.size() - 1));

        ipList = parser.parse("36.16.0.0\t36.37.31.255");
        assertEquals((37 - 16) + 32, ipList.size());
        for (int i = 16; i <= 36; i++)
            assertEquals("36." + i + ".*", ipList.get(i - 16));
        for (int i = 0; i <= 31; i++)
            assertEquals("36.37." + i + ".*", ipList.get(i + 21));

        ipList = parser.parse("42.0.128.0\t42.1.59.255");
        assertEquals(128 + 60, ipList.size());
        for (int i = 128; i <= 255; i++)
            assertEquals("42.0." + i + ".*", ipList.get(i - 128));
        for (int i = 0; i <= 59; i++)
            assertEquals("42.1." + i + ".*", ipList.get(i + 128));

        ipList = parser.parse("59.191.240.0\t60.31.255.255");
        for (int i = 240; i <= 255; i++) // 59.192.240.* ~ 59.191.255.*
            assertEquals("59.191." + i + ".*", ipList.get(i - 240));
        for (int i = 192; i <= 255; i++) // 59.192.* ~ 59.255.*
            assertEquals("59." + i + ".*", ipList.get(i - 192 + 16));
        for (int i = 0; i <= 30; i++) // 60.0.* ~ 60.31.*
            assertEquals("60." + i + ".*", ipList.get(i + 16 + (255 - 192 + 1)));

        // 42.156.36.0 ~ 42.187.123.255
        ipList = parser.parse("42.156.36.0\t42.187.123.255");
        for (int i = 36; i <= 255; i++) // 42.156.36.* ~ 42.156.255.*
            assertEquals("42.156." + i + ".*", ipList.get(i - 36));
        for (int i = 157; i <= 186; i++) // 42.157.* ~ 42.186.*
            assertEquals("42." + i + ".*", ipList.get((i - 157) + 220));
        for (int i = 0; i <= 123; i++) // 42.187.0.* ~ 42.187.123.*
            assertEquals("42.187." + i + ".*", ipList.get(i + 220 + 30));

        // 106.187.32.0 ~ 106.187.34.175
        ipList = parser.parse("106.187.32.0\t106.187.34.175");
        assertEquals("106.187.32.*", ipList.get(0));
        assertEquals("106.187.33.*", ipList.get(1));
        for (int i = 0; i <= 175; i++)
            assertEquals("106.187.34." + i, ipList.get(i + 2));

        // 106.187.34.177 ~ 106.187.39.255
        ipList = parser.parse("106.187.34.177\t106.187.39.255");
        for (int i = 177; i <= 255; i++) // 106.187.34.177 ~ 106.187.34.255
            assertEquals("106.187.34." + i, ipList.get(i - 177));
        for (int i = 35; i <= 39; i++) // 106.187.35.* ~ 106.187.39.*
            assertEquals("106.187." + i + ".*", ipList.get(i - 35 + 79));

        // 110.240.0.0 ~ 111.63.255.255
        ipList = parser.parse("110.240.0.0\t111.63.255.255");
        for (int i = 240; i <= 255; i++) // 110.240.* ~ 110.255.*
            assertEquals("110." + i + ".*", ipList.get(i - 240));
        for (int i = 0; i <= 63; i++) // 111.0.* ~ 111.63.*
            assertEquals("111." + i + ".*", ipList.get(i + 16));

        // 118.24.0.0 ~ 118.27.255.254
        ipList = parser.parse("118.24.0.0\t118.27.255.254");
        assertEquals("118.24.*", ipList.get(0));
        assertEquals("118.25.*", ipList.get(1));
        assertEquals("118.26.*", ipList.get(2));
        for (int i = 0; i < 255; i++)
            assertEquals("118.27." + i + ".*", ipList.get(i + 3));
        for (int i = 0; i <= 254; i++)
            assertEquals("118.27.255." + i, ipList.get(i + 3 + 255));

        // 118.244.0.0 ~ 119.2.31.255
        ipList = parser.parse("118.244.0.0\t119.2.31.255");
        for (int i = 244; i <= 255; i++) // 118.244.* ~ 118.255.*
            assertEquals("118." + i + ".*", ipList.get(i - 244));
        assertEquals("119.0.*", ipList.get(12)); // 119.0.* ~
        assertEquals("119.1.*", ipList.get(13)); // 119.1.*
        for (int i = 0; i <= 31; i++) // 119.2.0.* ~ 119.2.31.*
            assertEquals("119.2." + i + ".*", ipList.get(i + 14));
    }

}
