package org.lunifera.bpm.drools.common.server.commands;

import java.util.Map;

import org.jbpm.task.Group;
import org.jbpm.task.User;
import org.jbpm.task.service.TaskService;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddUsersAndGroupsCommand implements ITaskServiceCommand {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(AddUsersAndGroupsCommand.class);

	private Map<String, User> users;
	private Map<String, Group> groups;

	public AddUsersAndGroupsCommand(Map<String, User> users,
			Map<String, Group> groups) {
		super();
		this.users = users;
		this.groups = groups;
	}

	@Override
	public void execute(TaskService target, IBPMService service) {

		LOGGER.debug("Adding users and groups to task service");
		target.addUsersAndGroups(users, groups);
	}

}
