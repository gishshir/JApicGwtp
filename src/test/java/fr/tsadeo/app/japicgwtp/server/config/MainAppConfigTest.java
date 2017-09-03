package fr.tsadeo.app.japicgwtp.server.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.tsadeo.app.japicgwtp.server.manager.ProfileManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = fr.tsadeo.app.japicgwtp.server.config.MainAppConfigH2.class)
@Component
public class MainAppConfigTest {
	
	@Autowired
	private ProfileManager profileManager;
	
	@Test
	public void testNothing() {
		
		System.out.println("testNothing()");
	}
	
	@Test
	public void testProfileManager() {
		
		assertNotNull("ProfileManager cannot be null!", profileManager);
	}


}
