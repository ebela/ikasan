/*
 * $Id$  
 * $URL$
 * 
 * ====================================================================
 * Ikasan Enterprise Integration Platform
 * 
 * Distributed under the Modified BSD License.
 * Copyright notice: The copyright for this software and a full listing 
 * of individual contributors are as shown in the packaged copyright.txt 
 * file. 
 * 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 *  - Neither the name of the ORGANIZATION nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 */
package org.ikasan.dashboard.ui.topology.window;

import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.ikasan.dashboard.ui.mappingconfiguration.util.MappingConfigurationUISessionValueConstants;
import org.ikasan.error.reporting.model.ErrorOccurrence;
import org.ikasan.exclusion.model.ExclusionEvent;
import org.ikasan.security.service.authentication.IkasanAuthentication;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;

import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Ikasan Development Team
 *
 */
public class ExclusionEventViewWindow extends Window
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3347325521531925322L;
	
	private TextField roleName;
	private TextField roleDescription;
	private ExclusionEvent exclusionEvent;
	private ErrorOccurrence errorOccurrence;
	

	/**
	 * @param policy
	 */
	public ExclusionEventViewWindow(ExclusionEvent exclusionEvent, ErrorOccurrence errorOccurrence)
	{
		super();
		this.exclusionEvent = exclusionEvent;
		this.errorOccurrence = errorOccurrence;
		
		this.init();
	}


	public void init()
	{
		this.setModal(true);
		this.setResizable(false);
		this.setSizeFull();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		
		layout.addComponent( createWiretapDetailsPanel());
			
		this.setContent(layout);
	}

	protected Panel createWiretapDetailsPanel()
	{
		Panel exclusionEventDetailsPanel = new Panel("Exclusion Event");
		exclusionEventDetailsPanel.setSizeFull();
		exclusionEventDetailsPanel.setStyleName("dashboard");
		
		GridLayout layout = new GridLayout(2, 6);
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setColumnExpandRatio(0, 0.3f);
		layout.setColumnExpandRatio(1, 0.7f);
		layout.addComponent(new Label("Module Name"), 0, 0);
		
		TextField tf1 = new TextField();
		tf1.setValue(this.exclusionEvent.getModuleName());
		tf1.setReadOnly(true);
		tf1.setWidth("80%");
		layout.addComponent(tf1, 1, 0);
		
		layout.addComponent(new Label("Flow Name"), 0, 1);
		
		TextField tf2 = new TextField();
		tf2.setValue(this.exclusionEvent.getFlowName());
		tf2.setReadOnly(true);
		tf2.setWidth("80%");
		layout.addComponent(tf2, 1, 1);
		
		layout.addComponent(new Label("Event Id"), 0, 2);
		
		TextField tf3 = new TextField();
		tf3.setValue(this.errorOccurrence.getEventLifeIdentifier());
		tf3.setReadOnly(true);
		tf3.setWidth("80%");
		layout.addComponent(tf3, 1, 2);
		
		layout.addComponent(new Label("Date/Time"), 0, 3);
		
		TextField tf4 = new TextField();
		tf4.setValue(new Date(this.exclusionEvent.getTimestamp()).toString());
		tf4.setReadOnly(true);
		tf4.setWidth("80%");
		layout.addComponent(tf4, 1, 3);
		
		layout.addComponent(new Label("Error URI"), 0, 4);
		
		TextField tf5 = new TextField();
		tf5.setValue(exclusionEvent.getErrorUri());
		tf5.setReadOnly(true);
		tf5.setWidth("80%");
		layout.addComponent(tf5, 1, 4);
		
		Button resubmitButton = new Button("Re-submit");
		resubmitButton.addClickListener(new Button.ClickListener() 
    	{
            @SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) 
            {
            	IkasanAuthentication authentication = (IkasanAuthentication)VaadinService.getCurrentRequest().getWrappedSession()
        	        	.getAttribute(MappingConfigurationUISessionValueConstants.USER);
            	
            	HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "admin");
            	
            	ClientConfig clientConfig = new ClientConfig();
            	clientConfig.register(feature) ;
            	
            	Client client = ClientBuilder.newClient(clientConfig);
        	    WebTarget webTarget = client.target("http://svc-stewmi:8080/sample-scheduleDrivenSrc/rest/resubmission/submit/testFlowName");
        	    Response response = webTarget.request().put(Entity.entity("this is a test!", MediaType.APPLICATION_OCTET_STREAM));
            }
        });
		
		Button ignoreButton = new Button("Ignore");
		ignoreButton.addClickListener(new Button.ClickListener() 
    	{
            @SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) 
            {
            }
        });
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setHeight("100%");
		buttonLayout.setWidth(200, Unit.PIXELS);
		buttonLayout.setMargin(true);
		buttonLayout.addComponent(resubmitButton);
		buttonLayout.addComponent(ignoreButton);
		layout.addComponent(buttonLayout, 0, 5, 1, 5);
		
		GridLayout wrapperLayout = new GridLayout(1, 4);
		wrapperLayout.setMargin(true);
		wrapperLayout.setSizeFull();
		
		AceEditor editor = new AceEditor();
		editor.setCaption("Error Details");
		editor.setValue(this.errorOccurrence.getErrorDetail());
		editor.setReadOnly(true);
		editor.setMode(AceMode.xml);
		editor.setTheme(AceTheme.eclipse);
		editor.setWidth("100%");
		editor.setHeight(250, Unit.PIXELS);
		
		AceEditor eventEditor = new AceEditor();
		eventEditor.setCaption("Event Payload");
		eventEditor.setValue(new String((byte[])this.exclusionEvent.getEvent()));
		eventEditor.setReadOnly(true);
		eventEditor.setMode(AceMode.java);
		eventEditor.setTheme(AceTheme.eclipse);
		eventEditor.setWidth("100%");
		eventEditor.setHeight(250, Unit.PIXELS);

		HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setWidth("100%");
		formLayout.setHeight(140, Unit.PIXELS);
		formLayout.addComponent(layout);
		wrapperLayout.addComponent(formLayout, 0, 0);
		Label seperator = new Label("<hr />",ContentMode.HTML);
		wrapperLayout.addComponent(seperator, 0, 1);
		wrapperLayout.addComponent(eventEditor, 0, 2);
		wrapperLayout.setComponentAlignment(eventEditor, Alignment.TOP_LEFT);
		wrapperLayout.addComponent(editor, 0, 3);
		wrapperLayout.setComponentAlignment(editor, Alignment.TOP_LEFT);

		exclusionEventDetailsPanel.setContent(wrapperLayout);
		return exclusionEventDetailsPanel;
	}
}
