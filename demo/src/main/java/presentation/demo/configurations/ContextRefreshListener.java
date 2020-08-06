package presentation.demo.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import presentation.demo.services.UserService;
import presentation.demo.services.serviceImpl.UserServiceImpl;

@Component
public class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("APPLICATION IS RUNNIG !!!");
        this.userService.initApp();
    }
}
