package org.lunifera.bpm.drools.common.tasks.handler;

import org.drools.runtime.KnowledgeRuntime;
import org.drools.runtime.process.WorkItem;
import org.jbpm.task.Delegation;
import org.jbpm.task.Task;
import org.jbpm.task.TaskService;

/**
 *
 * This class provides the default configurations for a Mina WorkItem Handler
 */
public class LocalHTWorkItemHandler extends
		org.jbpm.process.workitem.wsht.LocalHTWorkItemHandler {

	public LocalHTWorkItemHandler(TaskService client, KnowledgeRuntime session) {
		super(client, session);
	}

	protected Task createTaskBasedOnWorkItemParams(WorkItem workItem) {
		Task task = super.createTaskBasedOnWorkItemParams(workItem);
		workItem.getParameter("inputType");
		task.setDelegation(new Delegation());
		return task;
	}

}
