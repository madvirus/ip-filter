package org.chimi.ipfilter.parser;


import org.chimi.ipfilter.Config;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UsingConfParserTestInJava {
    @Test
    public void useConfParser() {
        String confValue =
                "order deny,allow\n" +
                        "allow from 1.2.3.4\n" +
                        "deny from 10.20.30.40\n" +
                        "allow from 101.102.103.*\n" +
                        "allow from 201.202.203.10/64";

        Config config = new ConfParser().parse(confValue);
        assertFalse(config.isAllowFirst());
        assertEquals(config.getAllowList().size(), 3);
    }
}
