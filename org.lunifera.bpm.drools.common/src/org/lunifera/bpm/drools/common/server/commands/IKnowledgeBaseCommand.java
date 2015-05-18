package org.lunifera.bpm.drools.common.server.commands;

import org.drools.KnowledgeBase;
import org.lunifera.bpm.drools.common.server.IBPMService;

public interface IKnowledgeBaseCommand extends IBPMCommand<KnowledgeBase> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 * @param service
	 */
	void execute(KnowledgeBase target, IBPMService service);
}
