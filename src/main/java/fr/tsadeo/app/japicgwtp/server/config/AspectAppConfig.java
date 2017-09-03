package fr.tsadeo.app.japicgwtp.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//AOP annotation 
@EnableAspectJAutoProxy
@Configuration
@ComponentScan({ "fr.tsadeo.app.japicgwtp.server.aspect"})
public class AspectAppConfig {

}
