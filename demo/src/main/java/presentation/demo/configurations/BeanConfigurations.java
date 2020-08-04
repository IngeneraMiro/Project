package presentation.demo.configurations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import presentation.demo.utils.fileutil.FileUtil;
import presentation.demo.utils.fileutil.FileUtilImpl;

@Configuration
public class BeanConfigurations {

    @Bean
    public ModelMapper mapper(){
        return  new ModelMapper();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Gson gson(){
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public FileUtil fUtil(){
        return new FileUtilImpl();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler;
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        return taskScheduler;
    }
}
