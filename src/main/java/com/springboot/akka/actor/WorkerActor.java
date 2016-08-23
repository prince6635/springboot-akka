package com.springboot.akka.actor;

import akka.actor.UntypedActor;
import com.springboot.akka.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
* Step 4: The WorkerActor is a statefull actor that receives and sends messages (they have to be immutable)
* with other actors inside the onReceive method.
* Don't forget to use the unhandled method if the received message doesn't match.
 * Notice that actors have to be defined in Spring prototype scope.
* */
@Component("workerActor")
@Scope("prototype")
public class WorkerActor extends UntypedActor {

    @Autowired
    private BusinessService businessService;

    private int count = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Request) {
            String senderToReceiver = getSender() + "->" + getSelf();
            businessService.perform(senderToReceiver + ": " + this + " " + (++count));
        } else if (message instanceof Response) {
            String senderToReceiver = getSelf() + "->" + getSender();
            getSender().tell(senderToReceiver + ": " + count, getSelf());
        } else {
            unhandled(message);
        }
    }

    public static class Request {
    }

    public static class Response {
    }
}
