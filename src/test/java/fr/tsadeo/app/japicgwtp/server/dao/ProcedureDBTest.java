package fr.tsadeo.app.japicgwtp.server.dao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.tsadeo.app.japicgwtp.server.AbstractTest;
import fr.tsadeo.app.japicgwtp.server.config.MainAppConfig;
import fr.tsadeo.app.japicgwtp.server.dao.IApiDataTypeDao.DataTypeCriteria;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDataType;
import fr.tsadeo.app.japicgwtp.server.manager.DaoSessionManager;

/*
 * Tests sur la base Mysql des procedures stockees
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainAppConfig.class)
public class ProcedureDBTest extends AbstractTest {

	@Autowired
	private IApiDataTypeDao dataTypeDao;

	@Autowired
	private DaoSessionManager sessionManager;

	@Test
	public void testGetListApiDataTypeContainName() {

		this.sessionManager.beginTransaction();
		List<ApiDataType> result = this.dataTypeDao.listAllContainsNameForAvailableSites(new DataTypeCriteria("session"));
		
		assertNotNull("result cannot be null!", result);
		for (ApiDataType apiDataType : result) {
		   LOG.info(" --> " + apiDataType.getName());  
		}

		this.sessionManager.commitTransaction();
	}

	
	@Test
	public void testGetListApiDataTypeContainNameAndSite() {

		this.sessionManager.beginTransaction();
		List<ApiDataType> result = this.dataTypeDao.listAllContainsNameForAvailableSites(
				new DataTypeCriteria("session", "hib"));
		
		assertNotNull("result cannot be null!", result);
		for (ApiDataType apiDataType : result) {
		   LOG.info(" --> " + apiDataType.getName());  
		}

		this.sessionManager.commitTransaction();
	}

	@Test
	public void testGetListApiDataTypeContainNameAndSiteAndVersion() {

		this.sessionManager.beginTransaction();
		List<ApiDataType> result = this.dataTypeDao.listAllContainsNameForAvailableSites(
				new DataTypeCriteria("type", "jav", "1.6"));
		
		assertNotNull("result cannot be null!", result);
		for (ApiDataType apiDataType : result) {
		   LOG.info(" --> " + apiDataType.getName());  
		}

		this.sessionManager.commitTransaction();
	}
}
