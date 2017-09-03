/*
 * Copyright 2015 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package fr.tsadeo.app.japicgwtp.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface AppResources extends ClientBundle {

    interface Style extends CssResource {

    	@ClassName("app_iframe_panel")
        String appIframePanel();
        
    	@ClassName("app_result_panel")
        String appResultPanel();
        
    	@ClassName("app_input_search")
        String appInputSearch();
    	
    	@ClassName("app_type_label")
    	String appTypeLabel();
    	
    	@ClassName("app_result_widget")
    	String appResultWidget();
    	
    	@ClassName("app_relative_to_top")
    	String appRelativeToTop();
        
    	@ClassName("app_panel_error")
    	String appPanelError();
    	
    	@ClassName("app_form_button")
    	String appFormButton();
    	
    	@ClassName("app_form_button_next")
    	String appFormButtonNext();
    	
    	@ClassName("app_modal_content")
    	String appModalContent();
    	
    	@ClassName("app_iframe_title")
    	String appIFrameTitle();
    	
    }
    
 

    @Source("css/widget.gss")
    Style style();
}
