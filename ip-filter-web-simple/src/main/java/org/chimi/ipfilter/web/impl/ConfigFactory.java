package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.Config;

public abstract class ConfigFactory {
    public static ConfigFactory getInstance(String type) {
        if (type.equals("text")) {
            return new TextConfigFactory();
        }
        return null;
    }

    public abstract Config create(String value);
}
