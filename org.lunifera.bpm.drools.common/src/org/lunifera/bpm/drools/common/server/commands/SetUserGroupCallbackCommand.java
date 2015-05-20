package org.lunifera.bpm.drools.common.server.commands;

import org.jbpm.task.identity.UserGroupCallback;
import org.jbpm.task.identity.UserGroupCallbackManager;
import org.jbpm.task.service.TaskService;

public class SetUserGroupCallbackCommand implements ITaskServiceCommand {

	private UserGroupCallback callback;

	public SetUserGroupCallbackCommand(UserGroupCallback callback) {
		super();
		this.callback = callback;
	}

	@Override
	public void execute(TaskService target) {
		UserGroupCallbackManager.getInstance().setCallback(callback);
	}

}
