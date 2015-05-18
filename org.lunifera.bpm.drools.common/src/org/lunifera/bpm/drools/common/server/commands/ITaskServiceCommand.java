package org.lunifera.bpm.drools.common.server.commands;

import org.jbpm.task.service.TaskService;
import org.lunifera.bpm.drools.common.server.IBPMService;

public interface ITaskServiceCommand extends IBPMCommand<TaskService> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 * @param service
	 */
	void execute(TaskService target, IBPMService service);
}
