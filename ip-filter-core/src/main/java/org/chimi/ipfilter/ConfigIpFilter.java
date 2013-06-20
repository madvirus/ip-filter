package org.chimi.ipfilter;

import java.util.List;

public class ConfigIpFilter implements IpFilter {

    private boolean defaultAllow;
    private IpTree allowIpTree;
    private IpTree denyIpTree;
    private boolean allowFirst;

    public ConfigIpFilter(Config config) {
        defaultAllow = config.isDefaultAllow();
        allowFirst = config.isAllowFirst();
        allowIpTree = makeIpTree(config.getAllowList());
        denyIpTree = makeIpTree(config.getDenyList());
    }

    private IpTree makeIpTree(List<String> ipList) {
        IpTree ipTree = new IpTree();
        for (String ip : ipList)
            ipTree.add(ip);
        return ipTree;
    }

    @Override
    public boolean accept(String ip) {
        if (allowFirst) {
            if (allowIpTree.containsIp(ip)) return true;
            if (denyIpTree.containsIp(ip)) return false;
        } else {
            if (denyIpTree.containsIp(ip)) return false;
            if (allowIpTree.containsIp(ip)) return true;
        }
        return defaultAllow;
    }

}
