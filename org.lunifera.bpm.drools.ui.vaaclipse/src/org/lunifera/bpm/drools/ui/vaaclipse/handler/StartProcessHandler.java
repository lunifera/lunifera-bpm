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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.IDroolsSession;

public class StartProcessHandler {

	@Inject
	private IBPMService bpmService;

	@Inject
	@Named("user")
	@Optional
	String userId;

	@Execute
	public void execute(org.drools.definition.process.Process process,
			@Active IEclipseContext context) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", "Administrator");
		params.put("description", "Need a new laptop computer");

		IDroolsSession session = bpmService.createSession();
		StatefulKnowledgeSession kSession = session.getWrappedSession();
		ProcessInstance pi = kSession.startProcess(process.getId(), params);
		session.dispose();
	}

	@CanExecute
	public boolean canExecute(
			@Optional org.drools.definition.process.Process process) {
		return process != null;
	}
}
