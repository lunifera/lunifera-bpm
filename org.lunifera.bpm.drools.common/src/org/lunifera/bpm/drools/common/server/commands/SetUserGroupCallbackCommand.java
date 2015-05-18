package org.lunifera.bpm.drools.common.server.commands;

import org.jbpm.task.identity.UserGroupCallback;
import org.jbpm.task.identity.UserGroupCallbackManager;
import org.jbpm.task.service.TaskService;
import org.lunifera.bpm.drools.common.server.IBPMService;

public class SetUserGroupCallbackCommand implements ITaskServiceCommand {

	private UserGroupCallback callback;

	public SetUserGroupCallbackCommand(UserGroupCallback callback) {
		super();
		this.callback = callback;
	}

	@Override
	public void execute(TaskService target, IBPMService service) {
		UserGroupCallbackManager.getInstance().setCallback(callback);
	}

}
