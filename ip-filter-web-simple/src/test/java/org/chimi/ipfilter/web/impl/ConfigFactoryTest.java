package org.chimi.ipfilter.web.impl;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConfigFactoryTest {
    @Test
    public void getFileConfigFactory() {
        ConfigFactory factory = ConfigFactory.getInstance("file");
        assertTrue(factory instanceof FileConfigFactory);
    }

    @Test
    public void getClasspathConfigFactory() {
        ConfigFactory factory = ConfigFactory.getInstance("classpath");
        assertTrue(factory instanceof ClasspathConfigFactory);
    }

    @Test
    public void getTextConfigFactory() {
        ConfigFactory factory = ConfigFactory.getInstance("text");
        assertTrue(factory instanceof TextConfigFactory);
    }


}
