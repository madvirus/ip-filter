package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.web.IpBlocker;

public class FakeIpBlocker implements IpBlocker {
    public static final String ALLOW_IP = "1.2.3.4";

    public static boolean created;
    public static boolean reloaded;

    public static void reset() {
        created = false;
        reloaded = false;
    }

    public FakeIpBlocker() {
        created = true;
    }

    @Override
    public boolean accept(String remoteAddr) {
        return remoteAddr.equals(ALLOW_IP);
    }

    @Override
    public void reload() {
        reloaded = true;
    }
}
