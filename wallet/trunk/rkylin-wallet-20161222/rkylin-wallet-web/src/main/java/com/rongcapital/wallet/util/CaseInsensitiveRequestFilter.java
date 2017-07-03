package com.rongcapital.wallet.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * 
 * Description:  springmvc case-insensitive   parameters 
 * @author: bihf
 * @CreateDate: 2016年11月3日
 * @version: V1.0
 */
@Component
public class CaseInsensitiveRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new CaseInsensitiveHttpServletRequestWrapper(request), response);
    }

    private static class CaseInsensitiveHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final LinkedCaseInsensitiveMap params = new LinkedCaseInsensitiveMap();

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request
         * @throws IllegalArgumentException if the request is null
         */
        private CaseInsensitiveHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            params.putAll(request.getParameterMap());
            params.put("aaaa", "bbbb");
        }

        @Override
        public String getParameter(String name) {
            String[] values = getParameterValues(name);
            if (values == null || values.length == 0) {
                return null;
            }
            return values[0];
        }

        @Override
        public Map getParameterMap() {
            return Collections.unmodifiableMap(this.params);
        }

        @Override
        public Enumeration getParameterNames() {
            return Collections.enumeration(this.params.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return (String[]) params.get(name);
        }
    }
}