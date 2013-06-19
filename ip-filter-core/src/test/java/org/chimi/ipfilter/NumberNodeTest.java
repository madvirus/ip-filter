package org.chimi.ipfilter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NumberNodeTest {

    @Test
    public void createSimpleNode() {
        NumberNode node = new NumberNode("10");
        assertTrue(node.isSimpleNumber());
    }

    @Test
    public void createPatternNodeWith128_25() {
        NumberNode node = new NumberNode("128/25");
        assertFalse(node.isSimpleNumber());
        assertNodeMatch(node, 128, 255, true);
        assertNodeMatch(node, 0, 127, false);
    }

    private void assertNodeMatch(NumberNode node, int from, int to, boolean expected) {
        for (int i = from; i <= to; i++)
            assertEquals("test value = " + i, expected, node.isMatch(Integer.toString(i)));
    }

    @Test
    public void createPatternNodeWith8_30() {
        NumberNode node = new NumberNode("8/30");
        assertFalse(node.isSimpleNumber());
        assertNodeMatch(node, 8, 11, true);
        assertNodeMatch(node, 0, 7, false);
        assertNodeMatch(node, 12, 255, false);
    }
}
