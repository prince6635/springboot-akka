package com.springboot.akka.actor;

import akka.actor.UntypedActor;
import com.springboot.akka.model.Message;
import com.springboot.akka.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component("nonBlockingWorkerActor")
@Scope("prototype")
public class NonBlockingWorkerActor extends UntypedActor {

    @Autowired
    private BusinessService businessService;

    final private CompletableFuture<Message> future;

    public NonBlockingWorkerActor(CompletableFuture<Message> future) {
        this.future = future;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        String senderToReceiver = getSender() + "->" + getSelf();
        businessService.perform(senderToReceiver + ": " + this + " " + message);

        if (message instanceof Message) {
            future.complete((Message) message);
        } else {
            unhandled(message);
        }

        getContext().stop(self());
    }
}
