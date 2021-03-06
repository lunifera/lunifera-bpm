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
package org.lunifera.bpm.drools.ui.vaaclipse;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.jbpm.task.TaskService;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.ui.vaadin.ProcessField;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * View that shows problems.
 */
public class ProcessView {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessView.class);

	@Inject
	private IBPMService bpmService;
	@Inject
	private VerticalLayout parent;
	@Inject
	private IEclipseContext eclipseContext;
	@Inject
	private II18nService i18nService;
	@Inject
	private IResourceProvider resourceProvider;
	@Inject
	TaskService service;

	private ProcessField processField;

	@Inject
	public ProcessView() {
	}

	@SuppressWarnings("serial")
	@PostConstruct
	public void setup() {
		processField = new ProcessField(bpmService, i18nService,
				resourceProvider, false);
		processField.setSizeFull();
		parent.addComponent(processField);

		processField.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				org.drools.definition.process.Process value = (org.drools.definition.process.Process) event
						.getProperty().getValue();
				if (value != null) {
					eclipseContext.set(
							org.drools.definition.process.Process.class, value);
				} else {
					eclipseContext
							.remove(org.drools.definition.process.Process.class);
				}

			}
		});
	}

	public void refresh() {
		processField.refresh();
	}

}
