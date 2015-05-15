package org.lunifera.bpm.api;

import org.jbpm.task.service.TaskService;

public interface ITaskServiceCommand extends IBPMCommand<TaskService> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 * @param service
	 */
	void execute(TaskService target, IBPMService service);
}
