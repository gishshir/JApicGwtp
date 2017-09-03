package fr.tsadeo.app.japicgwtp.server.spring;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gwtplatform.dispatch.rpc.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.rpc.server.spring.HandlerModule;
import com.gwtplatform.dispatch.rpc.server.spring.LoggerFactoryBean;
import com.gwtplatform.dispatch.rpc.server.spring.actionvalidator.DefaultActionValidator;
import com.gwtplatform.dispatch.rpc.server.spring.configuration.DefaultModule;

import fr.tsadeo.app.japicgwtp.server.config.MainAppConfig;
import fr.tsadeo.app.japicgwtp.server.dispatch.ConnectOrGetUserActionHandler;
import fr.tsadeo.app.japicgwtp.server.dispatch.CreateOrUpdateSiteActionHandler;
import fr.tsadeo.app.japicgwtp.server.dispatch.DeleteItemHandler;
import fr.tsadeo.app.japicgwtp.server.dispatch.GetListDataTypeActionHandler;
import fr.tsadeo.app.japicgwtp.server.dispatch.GetListDataTypeForGridUpdateActionHandler;
import fr.tsadeo.app.japicgwtp.server.dispatch.GetListSiteActionHandler;
import fr.tsadeo.app.japicgwtp.server.dispatch.GetSiteForEditActionHandler;
import fr.tsadeo.app.japicgwtp.server.dispatch.ScanSiteActionHandler;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ConnectOrGetUserAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.CreateOrUpdateSiteAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.DeleteItemAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeForGridUpdateAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListSiteAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetSiteForEditAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ScanSiteAction;

/**
 * Module which binds the handlers and configurations.
 */
@Configuration
@Import({DefaultModule.class, MainAppConfig.class})
@ComponentScan(basePackages = "com.gwtplatform.dispatch.rpc.server.spring")
public class ServerModule extends HandlerModule {
	
	@Bean
	public DeleteItemHandler getDeleteItemHandler() {
		return new DeleteItemHandler();
	}
	
	@Bean
	public ConnectOrGetUserActionHandler getConnectOrGetUserActionHandler() {
		return new ConnectOrGetUserActionHandler();
	}
	
	@Bean
	public CreateOrUpdateSiteActionHandler getCreateOrUpdateSiteActionHandler() {
		return new CreateOrUpdateSiteActionHandler();
	}
	
	@Bean
	public GetSiteForEditActionHandler getGetSiteForEditActionHandler() {
		return new GetSiteForEditActionHandler();
	}
	
	@Bean
	public GetListDataTypeActionHandler getGetListDataTypeActionHandler() {
		return new GetListDataTypeActionHandler();
	}
	
	@Bean
	public GetListDataTypeForGridUpdateActionHandler getGetListDataTypeForGridUpdateActionHandler() {
		return new GetListDataTypeForGridUpdateActionHandler();
	}
	@Bean
	public GetListSiteActionHandler getGetListSiteActionHandler() {
		return new GetListSiteActionHandler();
	}
	
	@Bean
	public ScanSiteActionHandler getScanSiteActionHandler() {
		return new ScanSiteActionHandler();
	}
	
	
	@Bean
    public ActionValidator getDefaultActionValidator() {
        return new DefaultActionValidator();
    }

    @Bean
    public LoggerFactoryBean getLogger() {
        Logger logger = Logger.getAnonymousLogger();
        logger.setLevel(Level.FINEST);
        return new LoggerFactoryBean(logger);
    }

	
    @Override
    protected void configureHandlers() {
    	bindHandler(DeleteItemAction.class, DeleteItemHandler.class);
    	bindHandler(GetListDataTypeForGridUpdateAction.class, GetListDataTypeForGridUpdateActionHandler.class);
    	bindHandler(GetListDataTypeAction.class, GetListDataTypeActionHandler.class);
    	
    	bindHandler(GetListSiteAction.class, GetListSiteActionHandler.class);
    	bindHandler(ScanSiteAction.class, ScanSiteActionHandler.class);
    	bindHandler(GetSiteForEditAction.class, GetSiteForEditActionHandler.class);
    	bindHandler(CreateOrUpdateSiteAction.class, CreateOrUpdateSiteActionHandler.class);
    	bindHandler(ConnectOrGetUserAction.class, ConnectOrGetUserActionHandler.class);
    }
}