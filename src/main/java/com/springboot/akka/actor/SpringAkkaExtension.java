package com.springboot.akka.actor;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/*
* Step 2: an Akka Extension is used to add additional functionality to the ActorSystem.
* The SpringAkkaExtension uses Akka Props to create actors with the SpringActorProducer.
* */
@Component
public class SpringAkkaExtension implements Extension {

    private ApplicationContext applicationContext;

    public void initialize(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(String actorBeanName) {
        return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
    }
}
