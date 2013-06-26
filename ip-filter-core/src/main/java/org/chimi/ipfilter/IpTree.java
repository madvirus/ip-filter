package org.chimi.ipfilter;

public class IpTree {
    private NumberNode root = new NumberNode("");

    public void add(String ip) {
        String[] ipNumbers = ip.split("\\.");
        NumberNode node = root;
        for (String number : ipNumbers) {
            node = node.createOrGetChildNumber(number);
        }
    }

    public boolean containsIp(String ip) {
        String[] ipNumbers = ip.split("\\.");
        NumberNode node = root;
        for (String number : ipNumbers) {
            node = node.findMatchingChild(number);
            if (node == null)
                return false;
            if (node.isAllAccept())
                return true;
        }
        return true;
    }
}
