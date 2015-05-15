package org.lunifera.bpm.api;

import org.drools.runtime.StatefulKnowledgeSession;

public interface IStatefulSessionCommand extends IBPMCommand<StatefulKnowledgeSession> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 * @param service
	 */
	void execute(StatefulKnowledgeSession target, IBPMService service);
}
