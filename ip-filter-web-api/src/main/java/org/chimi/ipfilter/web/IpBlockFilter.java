package org.chimi.ipfilter.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class IpBlockFilter implements Filter {

    public static final String RELOAD_COMMAND_PARAM_NAME_CONFIG_NAME = "reloadCommandParamName";
    public static final String DEFAULT_RELOAD_COMMAND_PARAM_NAME_VALUE = "RCPN";

    private IpBlocker ipBlocker;
    private String reloadCommandParamName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        initializeReloadCommandParamName(filterConfig);
        createIpBlocker(filterConfig);
    }

    private void initializeReloadCommandParamName(FilterConfig filterConfig) {
        reloadCommandParamName = filterConfig.getInitParameter(RELOAD_COMMAND_PARAM_NAME_CONFIG_NAME);
        if (reloadCommandParamName == null)
            reloadCommandParamName = DEFAULT_RELOAD_COMMAND_PARAM_NAME_VALUE;
    }

    private void createIpBlocker(FilterConfig filterConfig) {
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
            processDoFilter(request, response, chain);
        else
            sendNotFoundResponse((HttpServletResponse) response);
    }

    private void processDoFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (checkReloadRequest((HttpServletRequest) request))
            reloadAndResponse(request, response);
        else
            chain.doFilter(request, response);
    }

    private boolean checkReloadRequest(HttpServletRequest request) {
        String reloadCommand = request.getParameter(reloadCommandParamName);
        if (reloadCommand == null) return false;
        return reloadCommand.compareToIgnoreCase("true") == 0;
    }

    private void reloadAndResponse(ServletRequest request, ServletResponse response) {
        ipBlocker.reload();
    }

    private void sendNotFoundResponse(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    public void destroy() {
    }
}
