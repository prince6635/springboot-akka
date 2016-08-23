package com.springboot.akka.controllor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.springboot.akka.actor.SpringAkkaExtension;
import com.springboot.akka.actor.WorkerActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

@RestController
public class AkkaController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringAkkaExtension springAkkaExtension;

    /*
    * It gets a WorkerActor from the ActorSystem inside the ApplicationContext,
    * to send sequence of requests and receive a response, and finally to terminate the ActorSystem.
    * Notice that the method Await.result is blocking, so it should be used in very limited cases
    * (in integration the actor-based part with the rest of the application or in unit tests).
    * */
    /*
    08/23 02:43:59.537 INFO [http-nio-8866-exec-1] c.s.a.c.AkkaController - Sender: Actor[akka://demo-actor-system/user/sender-worker-actor#73604498]
    08/23 02:43:59.537 INFO [http-nio-8866-exec-1] c.s.a.c.AkkaController - Receiver: Actor[akka://demo-actor-system/user/receiver-worker-actor#1420338626]
    08/23 02:43:59.540 INFO [demo-actor-system-akka.actor.default-dispatcher-4] c.s.a.s.BusinessService - Perform: Actor[akka://demo-actor-system/user/sender-worker-actor#73604498]->Actor[akka://demo-actor-system/user/receiver-worker-actor#1420338626]: com.springboot.akka.actor.WorkerActor@14fc6512 1 in Business service.
    08/23 02:43:59.540 INFO [demo-actor-system-akka.actor.default-dispatcher-4] c.s.a.s.BusinessService - Perform: Actor[akka://demo-actor-system/user/sender-worker-actor#73604498]->Actor[akka://demo-actor-system/user/receiver-worker-actor#1420338626]: com.springboot.akka.actor.WorkerActor@14fc6512 2 in Business service.
    08/23 02:43:59.540 INFO [demo-actor-system-akka.actor.default-dispatcher-4] c.s.a.s.BusinessService - Perform: Actor[akka://demo-actor-system/user/sender-worker-actor#73604498]->Actor[akka://demo-actor-system/user/receiver-worker-actor#1420338626]: com.springboot.akka.actor.WorkerActor@14fc6512 3 in Business service.
    08/23 02:43:59.541 INFO [http-nio-8866-exec-1] c.s.a.c.AkkaController - Response: Actor[akka://demo-actor-system/user/receiver-worker-actor#1420338626]->Actor[akka://demo-actor-system/temp/$a]: 3
    * */
    @RequestMapping("/akka")
    public String akka() throws Exception {
        try {
            ActorRef senderWorkerActor = actorSystem.actorOf(springAkkaExtension.props("workerActor"), "sender-worker-actor");
            ActorRef receiverWorkerActor = actorSystem.actorOf(springAkkaExtension.props("workerActor"), "receiver-worker-actor");
            logger.info("Sender: " + senderWorkerActor);
            logger.info("Receiver: " + receiverWorkerActor);

            receiverWorkerActor.tell(new WorkerActor.Request(), senderWorkerActor);
            receiverWorkerActor.tell(new WorkerActor.Request(), senderWorkerActor);
            receiverWorkerActor.tell(new WorkerActor.Request(), senderWorkerActor);

            FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
            Future<Object> awaitable = Patterns.ask(receiverWorkerActor, new WorkerActor.Response(), Timeout.durationToTimeout(duration));

            String response = "Response: " + Await.result(awaitable, duration);
            logger.info(response);

            return response;
        } finally {
            actorSystem.terminate();
            Await.result(actorSystem.whenTerminated(), Duration.Inf());
        }
    }
}
