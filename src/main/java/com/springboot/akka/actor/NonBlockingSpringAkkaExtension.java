package com.springboot.akka.actor;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NonBlockingSpringAkkaExtension implements Extension {

    private ApplicationContext applicationContext;

    public void initialize(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(String actorBeanName) {
        return Props.create(NonBlockingSpringActorProducer.class, applicationContext, actorBeanName);
    }

    public Props props(String actorBeanName, Object... args) {
        return Props.create(NonBlockingSpringActorProducer.class, applicationContext, actorBeanName, args);
    }
}
