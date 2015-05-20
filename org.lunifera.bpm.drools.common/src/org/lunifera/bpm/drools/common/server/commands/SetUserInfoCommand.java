package org.lunifera.bpm.drools.common.server.commands;

import org.jbpm.task.UserInfo;
import org.jbpm.task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetUserInfoCommand implements ITaskServiceCommand {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(SetUserInfoCommand.class);

	private UserInfo userInfo;

	public SetUserInfoCommand(UserInfo userInfo) {
		super();
		this.userInfo = userInfo;
	}

	@Override
	public void execute(TaskService target) {
		LOGGER.debug("Set userInfo to task service");
		target.setUserinfo(userInfo);
	}

}
