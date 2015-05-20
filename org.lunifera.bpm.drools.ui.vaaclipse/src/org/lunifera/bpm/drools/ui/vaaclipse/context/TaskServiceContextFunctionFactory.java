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
package org.lunifera.bpm.drools.ui.vaaclipse.context;

import javax.annotation.PreDestroy;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.jbpm.task.TaskService;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.osgi.service.component.annotations.Component;

@Component(service = org.eclipse.e4.core.contexts.IContextFunction.class, property = { "service.context.key=org.jbpm.task.TaskService" })
public class TaskServiceContextFunctionFactory extends ContextFunction {

	public TaskServiceContextFunctionFactory() {

	}

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		MApplication application = context.get(MApplication.class);
		IEclipseContext rootContext = application.getContext();

		TaskService client = rootContext.getLocal(TaskService.class);
		if (client == null) {
			// access the OSGi service
			IBPMService service = rootContext.get(IBPMService.class);
			if (service == null) {
				throw new IllegalStateException(
						"BpmService must be available as an OSGi service!");
			}
			client = service.createTaskClient();
			rootContext.set(TaskService.class, client);
		}
		return client;
	}

}
