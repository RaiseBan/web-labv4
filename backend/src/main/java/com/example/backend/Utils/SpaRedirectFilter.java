package com.example.backend.Utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpaRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();

        if (isApiRequest(uri) || isStaticResource(uri) || isRootRequest(uri)) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("/").forward(req, res);
        }
    }

    private boolean isApiRequest(String uri) {
        return uri.startsWith("/api/");
    }

    private boolean isStaticResource(String uri) {
        return uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/img/");
    }

    private boolean isRootRequest(String uri) {
        return uri.equals("/");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Нет необходимости в инициализации
    }

    @Override
    public void destroy() {
        // Нет необходимости в уничтожении
    }
}
