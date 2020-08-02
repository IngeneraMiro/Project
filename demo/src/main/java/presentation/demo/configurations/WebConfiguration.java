package presentation.demo.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import presentation.demo.interceptors.FaviconInterceptor;
import presentation.demo.interceptors.RestInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final FaviconInterceptor faviconInterceptor;
    private final RestInterceptor restInterceptor;


    public WebConfiguration(FaviconInterceptor faviconInterceptor, RestInterceptor restInterceptor) {
        this.faviconInterceptor = faviconInterceptor;
        this.restInterceptor = restInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.faviconInterceptor);
        registry.addInterceptor(this.restInterceptor);
    }


}
