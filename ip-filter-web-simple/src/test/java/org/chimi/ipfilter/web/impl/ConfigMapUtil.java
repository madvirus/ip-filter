package org.chimi.ipfilter.web.impl;

import java.util.HashMap;
import java.util.Map;

public class ConfigMapUtil {

    public static final String ALLOW_IP = "1.2.3.4";
    public static final String DENY_IP = "1.2.3.5";

    public static Map<String, String> getTextConfigMap() {
        Map<String, String> config = new HashMap<String, String>();
        config.put("type", "text");
        config.put("value",
                "order deny,allow\n" +
                        "allow from " + ALLOW_IP + "\n" +
                        "deny from " + DENY_IP);
        return config;
    }
}
