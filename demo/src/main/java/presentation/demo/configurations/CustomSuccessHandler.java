package presentation.demo.configurations;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
    public class CustomSuccessHandler implements AuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {



            String redirectUrl = null;

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                if (grantedAuthority.getAuthority().equals("ROLE_PATIENT")) {
                    redirectUrl = "/patient/patient-home";
                    break;
                } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                    redirectUrl = "/admin/admin-home";
                    break;
                }else if(grantedAuthority.getAuthority().equals("ROLE_DOCTOR")){
                    redirectUrl = "/doctor/doctor-home";
                }else if(grantedAuthority.getAuthority().equals("ROLE_NURSE")){
                    redirectUrl = "/nurse/nurse-home";
                }else{
                    redirectUrl = "/test";
                }
            }
            if (redirectUrl == null) {
                throw new IllegalStateException();
            }
            new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    }

