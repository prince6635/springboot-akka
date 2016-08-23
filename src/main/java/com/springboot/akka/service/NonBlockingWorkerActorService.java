package com.springboot.akka.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.springboot.akka.actor.NonBlockingSpringAkkaExtension;
import com.springboot.akka.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NonBlockingWorkerActorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private NonBlockingSpringAkkaExtension nonBlockingSpringAkkaExtension;

    /*
    * In the NonBlockingWorkerActorService method the NonBlockingWorkerActor is created with an incomplete CompletableFuture as a constructor parameter.
    * Notice how the Spring prototype-scope actor is injected into the singleton-scope CompletableFutureService.
    * Then a Message is sent to the NonBlockingWorkerActor with the tell method.
    * */
    public CompletableFuture<Message> get(String payload, Long id) {
        CompletableFuture<Message> future = new CompletableFuture<>();
        ActorRef senderWorkerActor = actorSystem.actorOf(nonBlockingSpringAkkaExtension.props("nonBlockingWorkerActor", future), "sender-worker-actor");
        ActorRef receiverWorkerActor = actorSystem.actorOf(nonBlockingSpringAkkaExtension.props("nonBlockingWorkerActor", future), "receiver-worker-actor");
        logger.info("Sender: " + senderWorkerActor);
        logger.info("Receiver: " + receiverWorkerActor);

        String senderToReceiver = senderWorkerActor + "->" + receiverWorkerActor;
        receiverWorkerActor.tell(new Message(payload, id, senderToReceiver), senderWorkerActor);
        return future;
   }
}
