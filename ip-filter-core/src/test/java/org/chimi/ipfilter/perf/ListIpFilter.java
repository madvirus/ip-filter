package org.chimi.ipfilter.perf;

import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.IpFilter;

import java.util.ArrayList;
import java.util.List;

public class ListIpFilter implements IpFilter {
    private boolean defaultAllow;
    private boolean allowFirst;
    private List<IpPattern> allowIpPatterns = new ArrayList<IpPattern>();
    private List<IpPattern> denyIpPatterns = new ArrayList<IpPattern>();

    public ListIpFilter(Config config) {
        defaultAllow = config.isDefaultAllow();
        allowFirst = config.isAllowFirst();
        for (String ipPattern : config.getAllowList())
            allowIpPatterns.add(new IpPattern(ipPattern));
        for (String ipPattern : config.getDenyList())
            denyIpPatterns.add(new IpPattern(ipPattern));
    }

    @Override
    public boolean accept(String ip) {
        if (allowFirst) {
            if (isAllowIp(ip)) return true;
            if (isDenyIp(ip)) return false;
        } else {
            if (isDenyIp(ip)) return false;
            if (isAllowIp(ip)) return true;
        }
        return defaultAllow;
    }

    private boolean isAllowIp(String ip) {
        for (IpPattern ipPattern : allowIpPatterns) {
            if (ipPattern.isMatch(ip))
                return true;
        }
        return false;
    }

    private boolean isDenyIp(String ip) {
        for (IpPattern ipPattern : denyIpPatterns) {
            if (ipPattern.isMatch(ip))
                return true;
        }
        return false;
    }

    private class IpPattern {

        private String exactMatchingPart;

        private boolean exactMatchingPattern = false;
        private boolean acceptAllPattern = false;
        private boolean rangePattern = false;

        private int fromNumberInRangePattern = 0;
        private int toNumberInRangePattern = 0;

        public IpPattern(String ipPattern) {
            if (ipPattern.endsWith("*")) {
                acceptAllPattern = true;
                exactMatchingPart = ipPattern.substring(0, ipPattern.length() - 1);
            } else {
                int slashIdx = ipPattern.indexOf("/");
                if (slashIdx == -1) {
                    exactMatchingPart = ipPattern;
                    exactMatchingPattern = true;
                } else {
                    int lastDotIdx = ipPattern.lastIndexOf(".");
                    exactMatchingPart = ipPattern.substring(0, lastDotIdx + 1);

                    int rangeNumber = Integer.parseInt(ipPattern.substring(lastDotIdx + 1, slashIdx));
                    int bitLength = Integer.parseInt(ipPattern.substring(slashIdx + 1));

                    rangePattern = true;
                    fromNumberInRangePattern = rangeNumber;
                    switch (bitLength) {
                        case 24:
                            toNumberInRangePattern = fromNumberInRangePattern + 0xFF;
                            break;
                        case 25:
                            toNumberInRangePattern = fromNumberInRangePattern + 0x7F;
                            break;
                        case 26:
                            toNumberInRangePattern = fromNumberInRangePattern + 0x3F;
                            break;
                        case 27:
                            toNumberInRangePattern = fromNumberInRangePattern + 0x1F;
                            break;
                        case 28:
                            toNumberInRangePattern = fromNumberInRangePattern + 0x0F;
                            break;
                        case 29:
                            toNumberInRangePattern = fromNumberInRangePattern + 0x07;
                            break;
                        case 30:
                            toNumberInRangePattern = fromNumberInRangePattern + 0x03;
                            break;
                    }
                }
            }
        }

        public boolean isMatch(String ip) {
            if (exactMatchingPattern)
                return ip.equals(exactMatchingPart);

            if (acceptAllPattern)
                return ip.startsWith(exactMatchingPart);

            if (rangePattern) {
                if (!ip.startsWith(exactMatchingPart)) return false;

                int lastNumberOfIp = Integer.parseInt(ip.substring(exactMatchingPart.length()));
                return lastNumberOfIp >= fromNumberInRangePattern && lastNumberOfIp <= toNumberInRangePattern;
            }
            return false;
        }
    }
}
