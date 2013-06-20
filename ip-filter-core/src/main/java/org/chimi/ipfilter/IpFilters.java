package org.chimi.ipfilter;

public class IpFilters {
    public static IpFilter create(Config config) {
        return new ConfigIpFilter(config);
    }

    public static IpFilter createCached(Config config) {
        return new CachedIpFilter(create(config));
    }
}
