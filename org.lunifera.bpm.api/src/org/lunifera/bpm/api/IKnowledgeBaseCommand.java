package org.lunifera.bpm.api;

import org.drools.KnowledgeBase;

public interface IKnowledgeBaseCommand extends IBPMCommand<KnowledgeBase> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 * @param service
	 */
	void execute(KnowledgeBase target, IBPMService service);
}
