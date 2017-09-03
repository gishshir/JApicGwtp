/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.manager;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;

/**
 *
 * @author sylvie
 */
public class SessionManagerTest extends AbstractDaoTestContext {
    
    @Test
    public void testXmlSpringConfiguration() {
        assertNotNull("logAspect cannot be null!", this.logAspect);
        this.logAspect.isAlive();
    }
    
    @Test
    public void testSessionManagerAutoInjection() {
        
        assertNotNull("sessionManager cannot be null!", this.sessionManager);
        this.sessionManager.verifySessionFactory();
        
    }
    
}
