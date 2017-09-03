package fr.tsadeo.app.japicgwtp.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import ({HibernateH2AppConfig.class, DaoAppConfig.class, ManagerAppConfig.class,
	ServiceAppConfig.class, AspectAppConfig.class, DomainAppConfig.class})
public class MainAppConfigH2 {

}
