package presentation.demo.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import presentation.demo.services.MessageService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static presentation.demo.global.GlobalConstants.FILE_ADDRESS;

@Component
public class ClearMessages {
    private final MessageService messageService;

    public ClearMessages(MessageService messageService){
        this.messageService = messageService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void clearMessages() throws IOException {
        this.messageService.clearMessages();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_ADDRESS, true));
            writer.write("Старите и прочетени съобщения бяха изтрити" + " " + formatter.format(LocalDateTime.now()));
            writer.newLine();
            writer.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void clearAllOldMessages() throws IOException {
        this.messageService.clearOldMessages();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_ADDRESS, true));
            writer.write("Съобщенията по стари от 3 месеца бяха изтрити" + " " + formatter.format(LocalDateTime.now()));
            writer.newLine();
            writer.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
