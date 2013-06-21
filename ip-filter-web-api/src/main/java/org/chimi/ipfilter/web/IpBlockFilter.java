package org.chimi.ipfilter.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class IpBlockFilter implements Filter {

    private IpBlocker ipBlocker;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ipBlocker = IpBlockerFactory.getInstance().create(filterConfigToMap(filterConfig));
    }

    private Map<String, String> filterConfigToMap(FilterConfig filterConfig) {
        Map<String, String> configMap = new HashMap<String, String>();
        Enumeration params = filterConfig.getInitParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            configMap.put(paramName, filterConfig.getInitParameter(paramName));
        }
        return configMap;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (ipBlocker.accept(request.getRemoteAddr()))
            chain.doFilter(request, response);
        else
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    public void destroy() {
    }
}
