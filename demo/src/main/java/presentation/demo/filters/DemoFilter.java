package presentation.demo.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;


@WebFilter(value = {"/patient/patient-home"})
public class DemoFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest myRequest = (HttpServletRequest) request;
        HttpServletResponse MyResponse = (HttpServletResponse) response;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String username = ((HttpServletRequest) request).getUserPrincipal().getName();
        System.out.printf("%s %s HAS BEEN LOGGED IN !!!!%n%n%n",formatter.format(LocalDateTime.now()),username);
        chain.doFilter(request, response);

    }
}
