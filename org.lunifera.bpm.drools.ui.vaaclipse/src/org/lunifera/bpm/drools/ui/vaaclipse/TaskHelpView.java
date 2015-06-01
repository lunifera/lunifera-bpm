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
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.jbpm.task.Task;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.lunifera.bpm.drools.ui.vaadin.TaskField;
import org.lunifera.bpm.drools.ui.vaadin.TaskHelpComponent;
import org.lunifera.runtime.common.event.IEventBroker;
import org.lunifera.runtime.common.help.IHelpService;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.VerticalLayout;

/**
 * View that shows problems.
 */
public class TaskHelpView {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TaskHelpView.class);

	@Inject
	private VerticalLayout parent;
	@Inject
	private IEclipseContext eclipseContext;
	@Inject
	private IHelpService helpService;
	@Inject
	private TaskService taskService;
	@Inject
	private IEventBroker eventBroker;

	private TaskHelpComponent taskHelpComponent;

	private EventHandlerImpl handler;

	@Inject
	public TaskHelpView() {
	}

	@PostConstruct
	public void setup() {
		taskHelpComponent = new TaskHelpComponent(helpService);
		taskHelpComponent.setSizeFull();
		taskHelpComponent.initialize();
		parent.addComponent(taskHelpComponent);

		handler = new EventHandlerImpl();
		eventBroker.subscribe(TaskField.EVENT_SELECTED_TASK, handler);
	}

	public void refresh() {
	}

	@PreDestroy
	public void dispose() {
		eventBroker.unsubscribe(handler);
		handler = null;
	}

	public void refreshContent(TaskSummary taskSummary) {
		Task task = taskService.getTask(taskSummary.getId());
		taskHelpComponent.setValue(task);
	}

	private class EventHandlerImpl implements EventHandler {

		@Override
		public void handleEvent(org.osgi.service.event.Event event) {
			TaskSummary taskSummary = (TaskSummary) event
					.getProperty(IEventBroker.DATA);
			refreshContent(taskSummary);
		}
	}
}
