package com.springboot.akka.controllor;

import com.springboot.akka.model.Message;
import com.springboot.akka.service.NonBlockingWorkerActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class NonBlockingAkkaController {

    private static final Long DEFERRED_RESULT_TIMEOUT = 1000L;

    private AtomicLong id = new AtomicLong(0);

    @Autowired
    private NonBlockingWorkerActorService nonBlockingWorkerActorService;

    /*
    * Logs:
    08/23 16:29:09.714 INFO [http-nio-8866-exec-1] c.s.a.s.NonBlockingWorkerActorService - Sender: Actor[akka://demo-actor-system/user/sender-worker-actor#-1324325052]
    08/23 16:29:09.714 INFO [http-nio-8866-exec-1] c.s.a.s.NonBlockingWorkerActorService - Receiver: Actor[akka://demo-actor-system/user/receiver-worker-actor#-496742796]
    08/23 16:29:09.726 DEBUG[http-nio-8866-exec-1] o.s.w.c.r.a.WebAsyncManager - Concurrent handling starting for GET [/non-blocking-akka]
    08/23 16:29:09.726 INFO [demo-actor-system-akka.actor.default-dispatcher-2] c.s.a.s.BusinessService - Perform: Actor[akka://demo-actor-system/user/sender-worker-actor#-1324325052]->Actor[akka://demo-actor-system/user/receiver-worker-actor#-496742796]: com.springboot.akka.actor.NonBlockingWorkerActor@2b1958af Message{payload='async-non-blocking', id=0, senderToReceiver=Actor[akka://demo-actor-system/user/sender-worker-actor#-1324325052]->Actor[akka://demo-actor-system/user/receiver-worker-actor#-496742796]} in Business service.
    08/23 16:29:09.726 DEBUG[http-nio-8866-exec-1] o.s.w.c.r.a.WebAsyncManager - Concurrent result value [Message{payload='async-non-blocking', id=0, senderToReceiver=Actor[akka://demo-actor-system/user/sender-worker-actor#-1324325052]->Actor[akka://demo-actor-system/user/receiver-worker-actor#-496742796]}] - dispatching request to resume processing
    * Web client:
    {
        "payload": "async-non-blocking",
        "id": 0,
        "senderToReceiver": "Actor[akka://demo-actor-system/user/sender-worker-actor#-1324325052]->Actor[akka://demo-actor-system/user/receiver-worker-actor#-496742796]"
    }
    * */
    @RequestMapping("/non-blocking-akka")
    public DeferredResult<Message> getAsyncNonBlocking() {
        DeferredResult<Message> deferredResult = new DeferredResult<>(DEFERRED_RESULT_TIMEOUT);
        CompletableFuture<Message> future = nonBlockingWorkerActorService.get("async-non-blocking", id.getAndIncrement());

        future.whenComplete((result, error) -> {
            if (error != null) {
                deferredResult.setErrorResult(error);
            } else {
                deferredResult.setResult(result);
            }
        });

        return deferredResult;
    }
}
