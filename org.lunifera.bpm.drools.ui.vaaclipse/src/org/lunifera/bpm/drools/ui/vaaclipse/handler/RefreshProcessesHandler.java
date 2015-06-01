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

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.jbpm.task.query.TaskSummary;
import org.lunifera.bpm.drools.ui.vaaclipse.ProcessView;

public class RefreshProcessesHandler {

	@Execute
	public void execute(@Active MPart part) {
		ProcessView view = (ProcessView) part.getObject();
		view.refresh();
	}

	@CanExecute
	public boolean canExecute(@Optional TaskSummary taskSummary) {
		return true;
	}
}
