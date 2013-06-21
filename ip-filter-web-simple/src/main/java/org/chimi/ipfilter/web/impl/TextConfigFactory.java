package org.chimi.ipfilter.web.impl;

import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.parser.ConfParser;

public class TextConfigFactory extends ConfigFactory {
    @Override
    public Config create(String value) {
        return new ConfParser().parse(value);
    }
}
