/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.bpmn.vaadin.bpmnio.sample;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

@SuppressWarnings("serial")
@VaadinServletConfiguration(ui = BpmnIoSampleUI.class, productionMode = false, widgetset = "org.lunifera.bpmn.vaadin.bpmnio.BpmnModelerWidgetset")
public class SimpleVaadinServlet extends VaadinServlet {

	@Override
	protected VaadinServletService createServletService(
			DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
		// see http://dev.vaadin.com/ticket/15516
		ServletService service = new ServletService(this,
				deploymentConfiguration);
		service.init();
		return service;
	}

}
