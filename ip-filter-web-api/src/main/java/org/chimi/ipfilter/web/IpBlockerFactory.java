package org.chimi.ipfilter.web;

import org.chimi.ipfilter.web.impl.IpBlockerFactoryImpl;

import java.util.Map;

public abstract class IpBlockerFactory {
    public static IpBlockerFactory getInstance() {
        return new IpBlockerFactoryImpl();
    }

    abstract public IpBlocker create(Map<String, String> config);
}
