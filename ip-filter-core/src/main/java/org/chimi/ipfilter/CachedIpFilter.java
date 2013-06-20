package org.chimi.ipfilter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CachedIpFilter implements IpFilter {

    private IpFilter delegate;
    private ConcurrentMap<String, Boolean> cachedMap = new ConcurrentHashMap<String, Boolean>();

    public CachedIpFilter(IpFilter delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean accept(String ip) {
        if (cachedMap.containsKey(ip))
            return cachedMap.get(ip);

        Boolean result = delegate.accept(ip);
        cachedMap.put(ip, result);
        return result;
    }
}
