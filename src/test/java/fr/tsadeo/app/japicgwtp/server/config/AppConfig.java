/*
 * Fichier de configuration de Spring
 * Creation automatique des beans contenus dans basePackages
 */
package fr.tsadeo.app.japicgwtp.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author sylvie
 */
@Configuration
@ComponentScan({ "fr.tsadeo.app.japicollector.server.config"})
public class AppConfig {
    
    
}
