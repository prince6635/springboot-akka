# Springboot with Akka

* Blocking
    * https://www.linkedin.com/pulse/spring-boot-akka-part-1-aliaksandr-liakh
    * The main purpose of this example was to illustrate how to get actors from Spring ApplicationContext. 
    But the drawback of this example was a blocking call between the actor-based part and the rest of the application. 
    Such usage can cease all Akka advantages in production applications.
* Non-blocking
    * https://www.linkedin.com/pulse/spring-boot-akka-part-2-aliaksandr-liakh
    * Use Akka in an asynchronous and non-blocking Java+Spring web-application.
    For this can be used asynchronous request processing in Spring MVC that is based on Servlet 3.0 specification.  
    Instead of returning a value, a @Controller method should return a DeferredResult or a Callable of the value. 
    In multi-tier applications, a @Service method should return a future (also known as promise, delay or deferred) - 
    a proxy to a value that isn’t completed yet. 
    There are some interfaces that have support for future processing in their frameworks:
        * java.util.concurrent.CompletableFuture (Java 8)
        * rx.Observable (RxJava)
        * org.springframework.util.concurrent.ListenableFuture (Spring Core)
        * com.google.common.util.concurrent.ListenableFuture (Google Guava)
    This example illustrates how to integrate such future interfaces in @Service methods with DeferredResult in @Controller methods.
    * The main difference with the previous application is that the WorkerActor has non-default constructor. 
    That required refactoring of SpringActorProducer and SpringAkkaExtension to have ability to pass the constructor arguments (Future).

* NOTE
    * akka logger
        * http://stackoverflow.com/questions/14149798/akka-slf4j-logback-configuration-and-usage
        * http://nidkil.me/2014/11/06/classnotfoundexception-when-adding-slf4j-to-akka/
    * errors
        * Error : "Usage of API documented as @since 1.7+..", 
        http://stackoverflow.com/questions/37787079/intellij-unable-to-use-newer-java-8-classes-error-usage-of-api-documented

