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
        if (isNumberValueStar())
            processAllPatternNumber();
        else if (isNumberValueNetworkPattern())
            processNetworkPatternNumber();
        else
            processSimpleNumber();
    }

    private boolean isNumberValueStar() {
        return number.equals("*");
    }

    private void processAllPatternNumber() {
        isSimpleNumber = false;
        allAccept = true;
    }

    private boolean isNumberValueNetworkPattern() {
        return number.indexOf("/") > 0;
    }

    private void processSimpleNumber() {
        isSimpleNumber = true;
    }

    private void processNetworkPatternNumber() {
        int slashIdx = number.indexOf("/");
        this.lastValueOfNetworkNumber = Integer.parseInt(number.substring(0, slashIdx));
        int bitsOfNetworkNumber = Integer.parseInt(number.substring(slashIdx + 1));

        this.filterNumber = filterNumbers[bitsOfNetworkNumber - 24];
        this.isSimpleNumber = false;
    }

    public NumberNode createOrGetChildNumber(String numberPattern) {
        NumberNode childNode = findAleadyExistingChildNumber(numberPattern);
        if (childNode != null)
            return childNode;

        return createChildNodeAndGet(numberPattern);
    }

    private NumberNode createChildNodeAndGet(String numberPattern) {
        NumberNode childNode = new NumberNode(numberPattern);
        if (childNode.isSimpleNumber)
            simpleChildNodeMap.put(numberPattern, childNode);
        else
            patternChildNodes.add(childNode);

        return childNode;
    }

    private NumberNode findAleadyExistingChildNumber(String numberPattern) {
        if (hasSimpleChildNode(numberPattern))
            return simpleChildNodeMap.get(numberPattern);
        else
            return findPatternBasedChildNumberNode(numberPattern);
    }

    private NumberNode findPatternBasedChildNumberNode(String numberPattern) {
        for (NumberNode patternChild : patternChildNodes)
            if (patternChild.number.equals(numberPattern))
                return patternChild;
        return null;
    }

    private boolean hasSimpleChildNode(String numberPattern) {
        return simpleChildNodeMap.containsKey(numberPattern);
    }

    public NumberNode findMatchingChild(String number) {
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
