package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.IpFilter;
import org.chimi.ipfilter.IpFilters;
import org.chimi.ipfilter.web.IpBlocker;

import java.util.Map;

public class IpBlockerImpl implements IpBlocker {

    private IpFilter ipFilter;

    public void init(Map<String, String> config) {
        Config conf = ConfigFactory.getInstance(config.get("type")).create(config.get("value"));
        ipFilter = IpFilters.createCached(conf);
    }

    @Override
    public boolean accept(String remoteAddr) {
        return ipFilter.accept(remoteAddr);
    }
}
