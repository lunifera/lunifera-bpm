package org.lunifera.bpm.drools.common.tasks.handler;

import org.drools.runtime.KnowledgeRuntime;
import org.drools.runtime.process.WorkItem;
import org.jbpm.process.workitem.wsht.GenericHTWorkItemHandler;
import org.jbpm.task.Delegation;
import org.jbpm.task.Task;
import org.jbpm.task.TaskService;
import org.jbpm.task.service.SyncTaskServiceWrapper;
import org.jbpm.task.service.mina.AsyncMinaTaskClient;
import org.jbpm.task.utils.OnErrorAction;

/**
 *
 * This class provides the default configurations for a Mina WorkItem Handler
 */
public class MinaHTWorkItemHandler extends GenericHTWorkItemHandler {
	private String connectorName = "SyncMinaHTWorkItemHandler";

	public MinaHTWorkItemHandler(KnowledgeRuntime session) {
		super(session);
		init();
	}

	public MinaHTWorkItemHandler(KnowledgeRuntime session,
			boolean owningSessionOnly) {
		super(session, owningSessionOnly);
		init();
	}

	public MinaHTWorkItemHandler(KnowledgeRuntime session, OnErrorAction action) {
		super(session, action);
		init();
	}

	public MinaHTWorkItemHandler(TaskService client, KnowledgeRuntime session) {
		super(client, session);
		init();
	}

	public MinaHTWorkItemHandler(TaskService client, KnowledgeRuntime session,
			boolean owningSessionOnly) {
		super(client, session, owningSessionOnly);
		init();
	}

	public MinaHTWorkItemHandler(String connectorName, TaskService client,
			KnowledgeRuntime session, OnErrorAction action) {
		super(session, action);
		this.connectorName = connectorName;
		setClient(client);
		init();
	}

	public MinaHTWorkItemHandler(String connectorName, TaskService client,
			KnowledgeRuntime session, OnErrorAction action,
			ClassLoader classLoader) {
		super(client, session, action, classLoader);
		this.connectorName = connectorName;
		setClient(client);
		init();
	}

	private void init() {
		if (getClient() == null) {
			setClient(new SyncTaskServiceWrapper(new AsyncMinaTaskClient(
					this.connectorName)));
		}
		if (getPort() <= 0) {
			setPort(9123);
		}
		if (getIpAddress() == null || getIpAddress().equals("")) {
			setIpAddress("127.0.0.1");
		}
	}

	protected Task createTaskBasedOnWorkItemParams(WorkItem workItem) {
		Task task = super.createTaskBasedOnWorkItemParams(workItem);
		task.setDelegation(new Delegation());
		return task;
	}

	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

}
