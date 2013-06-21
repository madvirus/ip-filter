package org.chimi.ipfilter.web;

public interface IpBlocker {
    boolean accept(String remoteAddr);
}
