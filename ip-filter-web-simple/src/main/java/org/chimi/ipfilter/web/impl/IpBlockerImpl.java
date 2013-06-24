package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.IpFilter;
import org.chimi.ipfilter.IpFilters;
import org.chimi.ipfilter.web.IpBlocker;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class IpBlockerImpl implements IpBlocker {

    private AtomicReference<IpFilter> ipFilter = new AtomicReference<IpFilter>();
    private ConfigFactory configFactory;
    private String value;

    public void init(Map<String, String> config) {
        configFactory = ConfigFactory.getInstance(config.get("type"));
        value = config.get("value");
        createIpFilterFromConfig();
    }

    private void createIpFilterFromConfig() {
        ipFilter.set(IpFilters.createCached(configFactory.create(value)));
    }

    @Override
    public boolean accept(String remoteAddr) {
        return ipFilter.get().accept(remoteAddr);
    }

    @Override
    public void reload() {
        if (!configFactory.isReloadSupported())
            throw new UnsupportedOperationException();
        createIpFilterFromConfig();
    }
}
