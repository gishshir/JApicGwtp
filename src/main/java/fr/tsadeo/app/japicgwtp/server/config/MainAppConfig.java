package fr.tsadeo.app.japicgwtp.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import ({HibernateAppConfig.class, DaoAppConfig.class, ManagerAppConfig.class,
	ServiceAppConfig.class, AspectAppConfig.class, DomainAppConfig.class})
public class MainAppConfig {

}
