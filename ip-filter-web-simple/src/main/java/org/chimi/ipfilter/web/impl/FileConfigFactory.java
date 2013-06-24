package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.parser.ConfParser;

import java.io.FileReader;
import java.io.IOException;

public class FileConfigFactory extends ConfigFactory {

    @Override
    public Config create(String value) {
        return new ConfParser().parse(readFromFile(value));
    }

    private String readFromFile(String fileName) {
        try {
            return IOUtil.read(new FileReader(fileName));
        } catch (IOException e) {
            throw new ConfigFactoryException(e);
        }
    }

    @Override
    public boolean isReloadSupported() {
        return true;
    }

}
