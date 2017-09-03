package fr.tsadeo.app.japicgwtp.client.application.search.datatype;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

import fr.tsadeo.app.japicgwtp.client.event.ItemSelectEvent;
import fr.tsadeo.app.japicgwtp.client.util.WidgetUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.JavaType;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDataTypeForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoHighlighText;
public class DataTypePresenter extends PresenterWidget<DataTypePresenter.MyView> implements DataTypeUiHandlers  {
	
	private static final Logger LOG = Logger.getLogger("DataTypePresenter");
	
    interface MyView extends View, HasUiHandlers<DataTypeUiHandlers>  {
		public void setJavaType(JavaType javaType);

		public void setSiteName(String siteName);

		public void setPackageName(String packageName);

		public void setDataTypeName(String datatypeName);

		public void setSelected(boolean selected);
		
		public void setVisible(boolean visible);
    }


    private VoDataTypeForGrid voDataType;
	private boolean selected = false;
	private boolean actif = true;
	
    //----------------------------------- constructor
    @Inject
    DataTypePresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);
        this.getView().setUiHandlers(this);
    }

  //---------------------------------------- implementing DataTypeUIHandlers
  	@Override
  	public void onSelectDataType() {
  		if (!this.selected) {
  			this.selected = !this.selected;
  			this.getView().setSelected(this.selected);
  			LOG.config("fire ItemSelectEvent");
  			ItemSelectEvent.fire(this, this.voDataType.getId());
  		}
  	}
      
    //------------------------------------- public methods
  	public void setActif(boolean actif) {
  		this.actif = actif;
  	}
  	public boolean isActif() {
  		return this.actif;
  	}
	public long getId() {
		return this.voDataType.getId();
	}

	public String getLink() {
		return this.voDataType.getLink();
	}

	public boolean isEframeAllowed() {
		return this.voDataType.isIframeAllowed();
	}
	public String getName() {
		return this.voDataType.getName();
	}
	public void updateHighlighedName(VoHighlighText voHighlighText, String search) {
		
		String htmlHighlightedName = WidgetUtils.buildHighlightedHtml(voDataType.getName(), 
				voHighlighText, search);
		this.getView().setDataTypeName(htmlHighlightedName);
	}
	public void setApiDataTypeResult(VoDataTypeForGrid voDataType, String search) {
		this.voDataType = voDataType;
		this.getView().setJavaType(voDataType.getType());
		this.getView().setSiteName(voDataType.getSiteName());
		this.getView().setPackageName(voDataType.getPackageName());
		
		this.updateHighlighedName(voDataType.getVoHighlightedName(), search);
	}

	public void unSelectIfSelected(long itemIdToBeSelected) {
		if (this.selected && itemIdToBeSelected != this.voDataType.getId()) {
			this.selected = false;
			this.getView().setSelected(false);
		}
	}

    
    
}