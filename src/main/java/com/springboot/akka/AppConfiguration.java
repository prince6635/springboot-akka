package com.springboot.akka;

import akka.actor.ActorSystem;
import com.springboot.akka.actor.NonBlockingSpringAkkaExtension;
import com.springboot.akka.actor.SpringAkkaExtension;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* step 3: a Spring @Configuration is used to provide the ActorSystem as a Spring bean.
* The ApplicaionConfiguration creates the ActorSystem from the Akka configuration overriding file application.conf and
* registers the SpringAkkaExtension in it.
* */
@Configuration
public class AppConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    /* for blocking one, need to change it back when switch to SpringAkkaExtension.
    @Autowired
    private SpringAkkaExtension springAkkaExtension;

    public ActorSystem actorSystem() {
        ActorSystem actorSystem = ActorSystem.create(
                "demo-actor-system", akkaConfiguration());
        springAkkaExtension.initialize(applicationContext);
        return actorSystem;
    }*/

    // for non-blocking one
    @Autowired
    private NonBlockingSpringAkkaExtension nonBlockingSpringAkkaExtension;

    @Bean(destroyMethod = "shutdown")
    public ActorSystem actorSystem() {
        ActorSystem actorSystem = ActorSystem.create(
                "demo-actor-system", akkaConfiguration());
        nonBlockingSpringAkkaExtension.initialize(applicationContext);
        return actorSystem;
    }

    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load();
    }
}
