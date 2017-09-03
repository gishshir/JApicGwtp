package fr.tsadeo.app.japicgwtp.server.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class AppConfigTest {
	
	
	@Test
	public void testNothing() {
		
		System.out.println("testNothing()");
	}

}
