package org.wintersleep.util.spring.tracer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TraceFilter implements Filter {

    private final CallTreeExecutionListener listener;

    public TraceFilter(CallTreeExecutionListener listener) {
        this.listener = listener;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            listener.start(httpServletRequest.getRequestURI());
            chain.doFilter(request, response);
        } finally {
            listener.stop();
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
