/*
 * Fichier de configuration de Spring
 */
package fr.tsadeo.app.japicgwtp.server.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

/**
 *
 * @author sylvie
 */
// Fichier de condiguration de Spring
@Configuration
// activation de la transaction
//@EnableTransactionManagement 
// AOP annotation 
@EnableAspectJAutoProxy
// localisation du fichier de properties
@PropertySource({ "classpath:persistence-h2.properties" })
// scan des component Spring pour creation automatique de bean et injection
@ComponentScan(basePackages = "fr.tsadeo.app.japicgwtp.server",
		excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = TotoBean.class))
//importation of xml spring configuration (complement)
//@ImportResource("classpath:/fr/tsadeo/app/japicollector/aspect.xml")
public class PersistenceConfig {
    
    @Autowired
    private Environment env;
    
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(this.restDataSource());
        // class domain model to map with hibernate
        sessionFactory.setPackagesToScan("fr.tsadeo.app.japicgwtp.server.domain");
        sessionFactory.setHibernateProperties(this.getHibernateProperties());
        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        
        HibernateTransactionManager  transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }
    
    @Bean
    public PersistenceExceptionTranslationPostProcessor exeptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    //----------------------------------------- private methods
    private DataSource restDataSource() {
       BasicDataSource dataSource = new BasicDataSource();
       dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
       dataSource.setUrl(env.getProperty("jdbc.url"));
       dataSource.setUsername(env.getProperty("jdbc.user"));
       dataSource.setPassword("jdbc.pass");
       return dataSource;
    }

    private Properties getHibernateProperties() {
        
        Properties prop = new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
                setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
                //setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
                setProperty("cache.provider_class", env.getProperty("cache.provider_class"));
                
                setProperty("hibernate.current_session_context_class", env.getProperty("hibernate.current_session_context_class"));
                setProperty("hibernate.use_identifier_rollback", env.getProperty("hibernate.use_identifier_rollback"));
                setProperty("connection.pool_size", env.getProperty("connection.pool_size"));
                
            }
        };
        
        return prop;
    }
    
}
