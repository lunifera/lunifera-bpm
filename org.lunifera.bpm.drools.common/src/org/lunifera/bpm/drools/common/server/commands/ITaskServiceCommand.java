package org.lunifera.bpm.drools.common.server.commands;

import org.jbpm.task.service.TaskService;

public interface ITaskServiceCommand extends IBPMCommand<TaskService> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 */
	void execute(TaskService target);
}
