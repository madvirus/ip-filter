package org.chimi.ipfilter;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private boolean defaultAllow;
    private boolean allowFirst;
    private List<String> allowList = new ArrayList<String>();
    private List<String> denyList = new ArrayList<String>();

    public void setDefaultAllow(boolean defaultAllow) {
        this.defaultAllow = defaultAllow;
    }

    public boolean isDefaultAllow() {
        return defaultAllow;
    }

    public void allow(String ip) {
        allowList.add(ip);
    }

    public void deny(String ip) {
        denyList.add(ip);
    }

    public void setAllowFirst(boolean allowFirst) {
        this.allowFirst = allowFirst;
    }


    public boolean isAllowFirst() {
        return allowFirst;
    }

    public List<String> getAllowList() {
        return allowList;
    }

    public List<String> getDenyList() {
        return denyList;
    }
}
