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

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.lunifera.bpm.drools.ui.vaaclipse.context.IDroolsContextConstants;
import org.lunifera.bpm.drools.ui.vaadin.TaskField;
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
public class TaskView {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TaskView.class);

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

	private TaskField taskField;

	@Inject
	public TaskView() {
	}

	@SuppressWarnings("serial")
	@PostConstruct
	public void setup() {
		taskField = new TaskField(service, i18nService, resourceProvider, false);
		taskField.setSizeFull();
		parent.addComponent(taskField);

		taskField.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				eclipseContext.set(TaskSummary.class, (TaskSummary) event
						.getProperty().getValue());
				eclipseContext.set(IDroolsContextConstants.ACTIVE_TASK_DATA,
						new HashMap<String, Object>());
			}
		});
	}

	public void refresh() {
		taskField.refresh();
	}

}
