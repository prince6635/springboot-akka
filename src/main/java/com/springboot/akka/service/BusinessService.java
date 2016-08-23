package com.springboot.akka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// The BusinessService is a simple service that is injected in the WorkerActor by Spring.
@Service
public class BusinessService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void perform(Object o) {
        logger.info("Perform: {} in Business service.", o);
    }
}
