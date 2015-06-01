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

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.drools.command.runtime.process.CompleteWorkItemCommand;
import org.drools.runtime.StatefulKnowledgeSession;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.utils.ContentMarshallerHelper;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.IDroolsSession;
import org.lunifera.bpm.drools.ui.vaaclipse.context.IDroolsContextConstants;

public class CompleteTaskHandler {

	@Inject
	private TaskService taskClient;

	@Inject
	private IBPMService bpmService;

	@Inject
	@Named("user")
	@Optional
	String userId;

	@SuppressWarnings("unchecked")
	@Execute
	public void execute(TaskSummary taskSummary, @Active IEclipseContext context) {
		Map<String, Object> data = (Map<String, Object>) context
				.get(IDroolsContextConstants.ACTIVE_TASK_DATA);

		Task task = taskClient.getTask(taskSummary.getId());
		TaskData taskData = task.getTaskData();
		long piId = taskData.getProcessInstanceId();

		IDroolsSession session = bpmService.createSession();
		try {

			StatefulKnowledgeSession kSession = session.getWrappedSession();
			WorkflowProcessInstanceImpl pInstance = (WorkflowProcessInstanceImpl) kSession
					.getProcessInstance(piId);

			ContentData contentData = createContentData(data);
			taskClient.complete(taskSummary.getId(), userId, contentData);

			pInstance = (WorkflowProcessInstanceImpl) kSession
					.getProcessInstance(piId);
			pInstance.getVariables();
			pInstance.getNodeInstances();
			CompleteWorkItemCommand command = new CompleteWorkItemCommand(task
					.getTaskData().getWorkItemId(), data);
			bpmService.execute(command, true);
		} finally {
			session.dispose();
		}
	}

	protected ContentData createContentData(Map<String, Object> data) {
		return ContentMarshallerHelper.marshal(data, null);
		// ContentData contentData = new ContentData();
		// if (data != null) {
		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// ObjectOutputStream outS;
		// if (data != null) {
		// try {
		// outS = new ObjectOutputStream(bos);
		// outS.writeObject(data);
		// outS.close();
		// } catch (IOException e) {
		// throw new IllegalStateException(e);
		// }
		// contentData.setContent(bos.toByteArray());
		// contentData.setAccessType(AccessType.Inline);
		// }
		// }
		// return contentData;
	}

	@CanExecute
	public boolean canExecute(@Optional TaskSummary taskSummary) {
		if (taskSummary == null) {
			return false;
		}
		return taskSummary.getStatus() == Status.InProgress;
	}
}