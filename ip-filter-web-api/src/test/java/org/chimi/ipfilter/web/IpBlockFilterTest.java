package org.chimi.ipfilter.web;

import javassist.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static org.mockito.Mockito.*;

public class IpBlockFilterTest {

    public static final String ALLOW_IP = "1.2.3.4";
    public static final String DENY_IP = "10.20.30.40";

    private FilterConfig mockFilterConfig;
    private Enumeration mockParamNameEnum;

    private IpBlockFilter filter;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private FilterChain mockFilterChain;

    @SuppressWarnings("unused")
    public static class FakeIpBlock implements IpBlocker {
        @Override
        public boolean accept(String remoteAddr) {
            return remoteAddr.equals(ALLOW_IP);
        }
    }

    @BeforeClass
    public static void initIpBlockerFactoryImplClass() throws CannotCompileException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("org.chimi.ipfilter.web.impl.IpBlockerFactoryImpl");
        CtMethod cm = cc.getDeclaredMethod("create", new CtClass[]{pool.get("java.util.Map")});
        cm.setBody("{ " +
                "return new org.chimi.ipfilter.web.IpBlockFilterTest.FakeIpBlock(); " +
                "}");
        cc.toClass();
    }

    @Before
    public void init() throws Exception {
        createMock();
        filter = new IpBlockFilter();
    }

    private void createMock() {
        mockFilterConfig = mock(FilterConfig.class);
        mockParamNameEnum = mock(Enumeration.class);
        when(mockFilterConfig.getInitParameterNames()).thenReturn(mockParamNameEnum);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockFilterChain = mock(FilterChain.class);
    }

    @Test
    public void filterInit() throws ServletException, IOException {
        filter.init(mockFilterConfig);
        verify(mockFilterConfig, only()).getInitParameterNames();
    }

    @Test
    public void shouldRunChainWhenIpIsNotBlocked() throws ServletException, IOException {
        filterInit();
        when(mockRequest.getRemoteAddr()).thenReturn(ALLOW_IP);

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
}
