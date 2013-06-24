package org.chimi.ipfilter.web;

import org.chimi.ipfilter.web.impl.FakeIpBlocker;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class IpBlockFilterTest {

    public static final String DENY_IP = "10.20.30.40";

    private FilterConfig mockFilterConfig;
    private Enumeration mockParamNameEnum;

    private IpBlockFilter filter;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private FilterChain mockFilterChain;

    @Before
    public void init() throws Exception {
        FakeIpBlocker.reset();

        createMock();
        filter = new IpBlockFilter();
    }

    private void createMock() {
        mockFilterConfig = mock(FilterConfig.class);
        when(mockFilterConfig.getInitParameter(IpBlockFilter.RELOAD_COMMAND_PARAM_NAME_CONFIG_NAME))
                .thenReturn("IBFRM");
        mockParamNameEnum = mock(Enumeration.class);
        when(mockFilterConfig.getInitParameterNames()).thenReturn(mockParamNameEnum);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockFilterChain = mock(FilterChain.class);
    }

    @Test
    public void filterInit() throws ServletException, IOException {
        assertFalse(FakeIpBlocker.created);
        filter.init(mockFilterConfig);
        assertTrue(FakeIpBlocker.created);
    }

    @Test
    public void shouldRunChainWhenIpIsNotBlocked() throws ServletException, IOException {
        filterInit();
        when(mockRequest.getRemoteAddr()).thenReturn(FakeIpBlocker.ALLOW_IP);
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain, only()).doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldResponse404WhenIpIsBlocked() throws ServletException, IOException {
        filterInit();
        when(mockRequest.getRemoteAddr()).thenReturn(DENY_IP);
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain, never()).doFilter(mockRequest, mockResponse);
        verify(mockResponse, only()).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void shouldReloadWhenRequestHasReloadParameter() throws ServletException, IOException {
        filterInit();
        when(mockRequest.getRemoteAddr()).thenReturn(FakeIpBlocker.ALLOW_IP);
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        assertFalse(FakeIpBlocker.reloaded);

        when(mockRequest.getParameter("IBFRM")).thenReturn("true");
        filter.doFilter(mockRequest, mockResponse, mockFilterChain);
        assertTrue(FakeIpBlocker.reloaded);
    }
}
