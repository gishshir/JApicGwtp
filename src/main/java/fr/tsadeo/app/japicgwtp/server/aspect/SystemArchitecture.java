/*
 * Definit les Pointcut des differents layers de l'application
 */
package fr.tsadeo.app.japicgwtp.server.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import fr.tsadeo.app.japicgwtp.shared.util.JApicException;

/**
 *
 * @author sylvie
 */
@Aspect
@Component
public class SystemArchitecture {// implements Ordered{
    
    private static final Log LOG = LogFactory.getLog(SystemArchitecture.class);
    //--------------------- implementing Ordered
//    @Override
//    public int getOrder() {
//     return 10;
//    }
    
    //=============================================
    //    POINT CUT
    //=============================================
    /**
     * A join point is in the service layer if the method is defined
     * in a type in the fr.tsadeo.app.japicgwtp.service package or any sub-package
     * under that.
     */
     @Pointcut ("within (fr.tsadeo.app.japicgwtp.server.service..*)")
//    @Pointcut ("execution(* fr.tsadeo.app.japicgwtp.service.*.*(..))")
    public void inServiceLayer() {}
    
     /**
     * A join point is in the dao layer if the method is defined
     * in a type in the fr.tsadeo.app.japicgwtp.dao package or any sub-package
     * under that.
     */
    @Pointcut ("within (fr.tsadeo.app.japicgwtp.server.dao..*)")
    public void inDataAccessLayer() {}
    
    /**
     * Combinaison of service & dao layer
     */
    @Pointcut ("inServiceLayer() && inDataAccessLayer()")
    public void inAllLayer() {}
    
     //=============================================
    //    ADVICES
    //=============================================
    @AfterThrowing (pointcut = "inServiceLayer()",
            throwing = "ex")
    public void doManageException (Throwable ex)  throws JApicException{
        LOG.info("doManageException()");
        if (ex instanceof JApicException) {
            throw (JApicException)ex;
        }
        // sinon on encapsule
        LOG.error("CREATE JApicException..." + ex.getMessage());
        throw new JApicException(ex);
    }



}
