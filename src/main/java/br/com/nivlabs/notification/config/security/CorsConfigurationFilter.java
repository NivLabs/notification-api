package br.com.nivlabs.notification.config.security;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author viniciosarodrigues
 * 
 * @since 19/11/2023
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfigurationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equals(req.getMethod())) {
            resp.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
            resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, X-Cutomer-Id");
            resp.setHeader("Access-Control-Max-Age", "3600");
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // Não é necessário implementar
    }

    @Override
    public void destroy() {
        // Não é necessário implementar
    }
}