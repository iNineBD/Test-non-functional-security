package com.javabycode.security;

import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
 
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
 
    public void commence(final HttpServletRequest request,
            final HttpServletResponse response, 
            final AuthenticationException authException) throws IOException, ServletException {
        //Authentication failed
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
         
    }
         
    @Override
    public void afterPropertiesSet() {
        setRealmName("MY_BASIC_AUTHENTICATION");
        super.afterPropertiesSet();
    }
}
