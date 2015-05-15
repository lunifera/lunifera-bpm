package org.lunifera.bpm.api;

import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.jbpm.task.service.TaskService;

public interface IBPMService {
	KnowledgeBuilder getKnowledgeBuilder();

	KnowledgeBase getKnowledgeBase();

	TaskService getTaskService();

	void startProcess(String processId, Map<String, Object> parameters,
			boolean logOn);

	void execute(ITaskServiceCommand command);

	void execute(IKnowledgeBaseCommand command);

	void execute(IStatefulSessionCommand command);

}
