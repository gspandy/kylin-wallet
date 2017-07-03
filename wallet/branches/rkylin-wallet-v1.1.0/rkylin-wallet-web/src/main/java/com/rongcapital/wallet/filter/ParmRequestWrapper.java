package com.rongcapital.wallet.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.LinkedCaseInsensitiveMap;

import com.rongcapital.wallet.controller.rop.constants.Constants;

public class ParmRequestWrapper extends HttpServletRequestWrapper {

    private final LinkedCaseInsensitiveMap params = new LinkedCaseInsensitiveMap();

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public ParmRequestWrapper(HttpServletRequest request) {
        super(request);

        params.putAll(request.getParameterMap());
        String url = request.getRequestURI();    
        if (url.indexOf(LoginFilter.ROP_URL) != -1) {
            params.remove(Constants.SYS_PARAM_FORMAT);
            params.remove(Constants.SYS_PARAM_SESSION);
            params.remove(Constants.SYS_PARAM_TIMESTAMP);
            params.remove(Constants.SYS_PARAM_APP_KEY);
            params.remove(Constants.SYS_PARAM_SIGN);   
        }
       
//        for (String key : mapParm.keySet()) {
//            if (!Constants.mapSysKey.containsKey(key.trim())) {
//                params.put(key, mapParm.get(key));
//            }
//        }
       
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
