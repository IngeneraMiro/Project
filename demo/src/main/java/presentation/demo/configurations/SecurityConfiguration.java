package presentation.demo.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import presentation.demo.services.serviceImpl.UserServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder encoder;
    private final UserServiceImpl userServiceImpl;
    private final CustomSuccessHandler successHandler;
    private final CustomLoginFailureHandler loginFailureHandler;

    public SecurityConfiguration(PasswordEncoder encoder, UserServiceImpl userServiceImpl, CustomSuccessHandler successHandler, CustomLoginFailureHandler loginFailureHandler) {
        this.encoder = encoder;
        this.userServiceImpl = userServiceImpl;
        this.successHandler = successHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider impl = new DaoAuthenticationProvider();
        impl.setUserDetailsService(userServiceImpl);
        impl.setPasswordEncoder(encoder);//new BCryptPasswordEncoder());
        impl.setHideUserNotFoundExceptions(false) ;
        return impl;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userServiceImpl).passwordEncoder(encoder);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/index","/info/**","/test","/css/**","/js/**","/images/**","/users/**").permitAll()
                .antMatchers("/practices/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/users/login").successHandler(successHandler).failureHandler(loginFailureHandler)
                .and().logout()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/?logout=true")
                .and().csrf().disable();

    }
}
