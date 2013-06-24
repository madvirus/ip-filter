package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.parser.ConfParser;

import java.io.IOException;
import java.io.InputStreamReader;

public class ClasspathConfigFactory extends ConfigFactory {
    @Override
    public Config create(String value) {
        return new ConfParser().parse(readFromClasspath(value));
    }

    private String readFromClasspath(String classpath) {
        try {
            return IOUtil.read(new InputStreamReader(getClass().getResourceAsStream(classpath)));
        } catch (IOException e) {
            throw new ConfigFactoryException(e);
        }
    }

    @Override
    public boolean isReloadSupported() {
        return false;
    }
}
