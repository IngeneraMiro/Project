package presentation.demo.configurations;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message = "";

        if(exception.getClass() == UsernameNotFoundException.class) {
            message = exception.getMessage();
        } else if(exception.getClass() == BadCredentialsException.class) {
            message = "Моля въведете в полето валидна парола !!!";
        }

        response.sendRedirect(String.format("/users/login?message=%s", URLEncoder.encode(message, StandardCharsets.UTF_8)));

    }
}