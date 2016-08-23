package com.springboot.akka.actor;


import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;

public class NonBlockingSpringActorProducer implements IndirectActorProducer {

    final private ApplicationContext applicationContext;
    final private String actorBeanName;
    final private Object[] args;

    public NonBlockingSpringActorProducer(ApplicationContext applicationContext, String actorBeanName, Object... args) {
        this.applicationContext = applicationContext;
        this.actorBeanName = actorBeanName;
        this.args = args;
    }

    @Override
    public Actor produce() {
        if (args == null) {
            return (Actor) applicationContext.getBean(actorBeanName);
        } else {
            return (Actor) applicationContext.getBean(actorBeanName, args);
        }
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
    }
}
