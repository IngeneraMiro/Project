package presentation.demo.configurations;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import presentation.demo.customevents.MessageEvent;

@Component
public class LeftMessageListener implements ApplicationListener<MessageEvent> {

    @Override
    public void onApplicationEvent(MessageEvent event) {
        String print = event.getSender()+" " + event.getMessage();
        System.out.println(print);
    }
}
