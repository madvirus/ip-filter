IP-FILTER
=========

## Overview

Very simple module to allow/deny ip

## Module list

| Module Name | Description | Required Module |
| --- | --- | --- |
| ip-filter-core | Provide core api for ip filtering | - |
| ip-filter-conf-parser | Provide parser for parsing string to Config object | ip-filter-core |
| ip-filter-web-api | Provide Web Filter API for blocking acess of denied ip | - |
| ip-filter-web-simple | Provide simple implementation of ip-filter-web-api | ip-filter-web-simple ip-filter-conf-parser ip-filter-core |

## Maven Configuration

ip-simple module is provided 

```
    <repositories>
        <!-- repository for ip-filter module -->
        <repository>
            <id>ip-filter-mvn-repo</id>
            <url>https://raw.github.com/madvirus/ip-filter/mvn-repo/</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>org.chimi.ipfilter</groupId>
            <artifactId>ip-filter-core</artifactId>
            <version>0.1</version>
        </dependency>
    </dependencies>
```

## ip-filter-core

### Usage

```java
import org.chimi.ipfilter.Config;
import org.chimi.ipfilter.IpFilter;
import org.chimi.ipfilter.IpFilters;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IpFilterTest {
    @Test
    public void basicUsageOfIpFilter() {
        // initialize Config
        Config config = new Config();
        config.setAllowFirst(true);
        config.setDefaultAllow(false);
        config.allow("1.2.3.4"); // add allow ip
        config.allow("10.20.30.40");
        config.deny("101.102.103.104"); // add deny ip
        
        // create IpFilter by using IpFilters.create() method
        IpFilter ipFilter = **IpFilters.create(config)**;
        // then, call accept method for filtering ip.
        assertTrue(**ipFilter.accept("1.2.3.4")**);
        assertFalse(ipFilter.accept("101.102.103.104"));
    }
}

```

### IP pattern
Available string for parameter value of allow/deny method of Config class

* simple ip: `1.2.3.4` (exact matching)
* network range: `1.1.1.64/26` (1.1.1.26 ~ 1.1.1.127), 00000001.00000001.00000001.01 (26bit)
* all: `1.2.3.*`, `1.2.*`, `1.*`, `*`

## ip-filter-conf-parser

ip-filter-conf-parser convert



## ip-filter-web-simple
