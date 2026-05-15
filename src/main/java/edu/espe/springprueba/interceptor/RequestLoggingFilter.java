package edu.espe.springprueba.interceptor;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestLoggingFilter implements Filter {

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Tarea 4: Contar peticiones
        int currentCount = counter.incrementAndGet();
        long t0 = System.currentTimeMillis();

        resp.addHeader("X-Request-Count", String.valueOf(currentCount));

        chain.doFilter(request, response);

        long elapsed = System.currentTimeMillis() - t0;

        System.out.println("Request #" + currentCount + " -> " + req.getMethod() +
                " " + req.getRequestURI() + " " + elapsed + "ms");
    }
}