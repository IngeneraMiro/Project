package presentation.demo.configurations;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import presentation.demo.customevents.MessageEvent;
import presentation.demo.customevents.MyCustomEvent;

@Component
public class CustomEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;


    public CustomEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishEvent(String text){
        MyCustomEvent myCustomEvent = new MyCustomEvent(this,text);
        this.applicationEventPublisher.publishEvent(myCustomEvent);
    }

    public void publishMessageNotification(String message,String sender){
        MessageEvent messageEvent = new MessageEvent(this,message,sender);
        this.applicationEventPublisher.publishEvent(messageEvent);
    }
}
