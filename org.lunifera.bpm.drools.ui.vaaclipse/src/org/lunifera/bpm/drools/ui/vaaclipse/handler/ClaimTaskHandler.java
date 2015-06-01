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
package org.lunifera.bpm.drools.ui.vaaclipse.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.jbpm.task.Status;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;

public class ClaimTaskHandler {

	@Inject
	private TaskService taskClient;

	@Inject
	@Named("user")
	@Optional
	String userId;

	@Execute
	public void execute(TaskSummary taskSummary) {
		taskClient.claim(taskSummary.getId(), userId);
	}

	@CanExecute
	public boolean canExecute(@Optional TaskSummary taskSummary) {
		if (taskSummary == null) {
			return false;
		}
		return taskSummary.getStatus() == Status.Created;
	}
}
