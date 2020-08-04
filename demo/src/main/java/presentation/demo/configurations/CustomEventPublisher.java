package presentation.demo.configurations;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import presentation.demo.listeners.MyCustomEvent;

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
}
