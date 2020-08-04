package presentation.demo.configurations;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import presentation.demo.listeners.MyCustomEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static presentation.demo.global.GlobalConstants.FILE_ADDRESS;

@Component
public class MyCustomEvenListener implements ApplicationListener<MyCustomEvent> {
    @Override
    public void onApplicationEvent(MyCustomEvent event) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_ADDRESS, true));
            writer.write(event.getMessage() + " " + formatter.format(LocalDateTime.now()));
            writer.newLine();
            writer.close();
        }catch (IOException e)   {
            System.out.println("Log file not found.");
        }
    }
}
