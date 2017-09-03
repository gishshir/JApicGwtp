/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import fr.tsadeo.app.japicgwtp.server.ILoggable;

/**
 *
 * @author sylvie
 */
@Component
@Aspect
public class LogAspect implements Ordered{
    
    private static final Log LOG = LogFactory.getLog(LogAspect.class);
    
    //------------------------------------ implementing Ordered
    @Override
    public int getOrder() {
        return 100;
    }
    //---------------------------------- public methods
    public boolean isAlive() {
        return true;
    }
    //=============================================
    //    POINT CUT
    //=============================================
    
    /**
     * The target object implements the ILoggable interface:
     */
    @Pointcut ("target(fr.tsadeo.app.japicgwtp.server.ILoggable)")
    public void loggableType() {}

    //=============================================
    //    ADVICES
    //=============================================
    
    @Before ("fr.tsadeo.app.japicgwtp.server.aspect.SystemArchitecture.inServiceLayer()" +
            "&& target(bean) && loggableType()")
    public void doLogMethodCall(JoinPoint js, ILoggable bean) {
        
        String message = "METHOD: " + js.getSignature(); 
        if (bean != null) {
            bean.getLog().info(message);
        } else {
            LOG.info(message);
        }
    }
  
}
