package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.Config;

public abstract class ConfigFactory {
    public static ConfigFactory getInstance(String type) {
        if (type.equals("text"))
            return new TextConfigFactory();

        if (type.equals("file"))
            return new FileConfigFactory();

        if (type.equals("classpath"))
            return new ClasspathConfigFactory();

        return null;
    }

    public abstract Config create(String value);

    public abstract boolean isReloadSupported();
}
