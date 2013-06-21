package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.web.IpBlocker;
import org.chimi.ipfilter.web.IpBlockerFactory;

import java.util.Map;

public class IpBlockerFactoryImpl extends IpBlockerFactory {
    @Override
    public IpBlocker create(Map<String, String> config) {
        throw new IllegalStateException("IpBlockerFactoryImpl must not be included in ip-filter-web-api.jar");
    }
}
