package org.chimi.ipfilter;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class CachedIpFilterTest {
    @Test
    public void shouldCacheDupIp() {
        IpFilter mockFilter = mock(IpFilter.class);
        String ip = "1.2.3.4";
        when(mockFilter.accept(ip)).thenReturn(true);

        CachedIpFilter filter = new CachedIpFilter(mockFilter);
        filter.accept(ip);

        verify(mockFilter, only()).accept(ip);

        filter.accept(ip);

        verify(mockFilter, only()).accept(ip);
    }
}
