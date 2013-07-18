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
        int[] numbers = splitNumberValueBySlash();
        this.lastValueOfNetworkNumber = numbers[0];
        int bitsOfNetworkNumber = numbers[1];
        this.filterNumber = filterNumbers[bitsOfNetworkNumber - 24];
        this.isSimpleNumber = false;
    }

    private int[] splitNumberValueBySlash() {
        int slashIdx = number.indexOf("/");
        return new int[]{
                Integer.parseInt(number.substring(0, slashIdx)),
                Integer.parseInt(number.substring(slashIdx + 1))};
    }

    public NumberNode createOrGetChildNumber(String numberPattern) {
        NumberNode childNode = findAleadyExistingChildNumber(numberPattern);
        if (childNode != null)
            return childNode;

        return createChildNodeAndGet(numberPattern);
    }

    private NumberNode findAleadyExistingChildNumber(String numberPattern) {
        if (hasSimpleChildNode(numberPattern))
            return simpleChildNodeMap.get(numberPattern);
        else
            return findPatternBasedChildNumberNode(numberPattern);
    }

    private boolean hasSimpleChildNode(String numberPattern) {
        return simpleChildNodeMap.containsKey(numberPattern);
    }

    private NumberNode createChildNodeAndGet(String numberPattern) {
        NumberNode childNode = new NumberNode(numberPattern);
        addChildNode(childNode);
        return childNode;
    }

    private void addChildNode(NumberNode childNode) {
        if (childNode.isSimpleNumber())
            simpleChildNodeMap.put(childNode.number, childNode);
        else
            patternChildNodes.add(childNode);
    }

    private NumberNode findPatternBasedChildNumberNode(String numberPattern) {
        for (NumberNode patternChild : patternChildNodes)
            if (patternChild.numberEquals(numberPattern))
                return patternChild;
        return null;
    }

    private boolean numberEquals(String numberPattern) {
        return number.equals(numberPattern);
    }

    public NumberNode findMatchingChild(String number) {
        if (hasSimpleChildNode(number))
            return simpleChildNodeMap.get(number);
        else
            return findMatchingChildInPatterns(number);
    }

    private NumberNode findMatchingChildInPatterns(String number) {
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
