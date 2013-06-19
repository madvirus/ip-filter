package org.chimi.ipfilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberNode {
    private Map<String, NumberNode> simpleChildNodeMap = new HashMap<String, NumberNode>();
    private List<NumberNode> patternChildNodes = new ArrayList<NumberNode>();

    private final String number;

    private boolean isSimpleNumber;
    private int filterNumber;
    private int lastValueOfNetworkNumber;
    private boolean allAccept;

    public NumberNode(String number) {
        this.number = number;
        processPattern();
    }

    private static int[] filterNumbers = {
            0x00, // 24
            0x80, // 25
            0xC0, // 26
            0xE0, // 27
            0xF0, // 28
            0xF8, // 29
            0xFC // 30
    };

    private void processPattern() {
        if (number.equals("*")) {
            isSimpleNumber = false;
            allAccept = true;
            return;
        }
        int slashIdx = number.indexOf("/");
        if (slashIdx == -1) {
            isSimpleNumber = true;
            return;
        }

        this.lastValueOfNetworkNumber = Integer.parseInt(number.substring(0, slashIdx));
        int bitsOfNetworkNumber = Integer.parseInt(number.substring(slashIdx + 1));

        this.filterNumber = filterNumbers[bitsOfNetworkNumber - 24];
        this.isSimpleNumber = false;
    }

    public NumberNode createOrGetChildNumber(String number) {
        if (simpleChildNodeMap.containsKey(number))
            return simpleChildNodeMap.get(number);

        NumberNode childNode = new NumberNode(number);
        if (childNode.isSimpleNumber)
            simpleChildNodeMap.put(number, childNode);
        else
            patternChildNodes.add(childNode);

        return childNode;
    }

    public NumberNode findChildNumber(String number) {
        NumberNode simpleChildNode = simpleChildNodeMap.get(number);
        if (simpleChildNode != null) return simpleChildNode;

        for (NumberNode patternChildNode : patternChildNodes)
            if (patternChildNode.isMatch(number))
                return patternChildNode;

        return null;
    }

    public boolean isMatch(String number) {
        if (allAccept) return true;
        if (isSimpleNumber) return this.number.equals(number);

        int filtered = filterNumber & Integer.parseInt(number);
        return filtered == lastValueOfNetworkNumber;
    }

    public boolean isSimpleNumber() {
        return isSimpleNumber;
    }

    public boolean isAllAccept() {
        return allAccept;
    }
}
