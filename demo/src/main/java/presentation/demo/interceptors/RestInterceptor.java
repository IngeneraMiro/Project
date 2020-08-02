package presentation.demo.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import presentation.demo.configurations.annotations.RestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static presentation.demo.global.GlobalConstants.FILE_ADDRESS;

@Component
public class RestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {


        if(handler instanceof HandlerMethod){

            RestMethod method = ((HandlerMethod) handler).getMethodAnnotation(RestMethod.class);
            if(method!=null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_ADDRESS,true));
                writer.write(method.value() + " " + formatter.format(LocalDateTime.now()));
                writer.newLine();
                writer.close();
            }
        }
    }
}
