package org.lunifera.bpm.drools.common.server.commands;

import org.drools.KnowledgeBase;

public interface IKnowledgeBaseCommand extends IBPMCommand<KnowledgeBase> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 */
	void execute(KnowledgeBase target);
}
