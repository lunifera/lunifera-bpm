package org.lunifera.bpm.drools.common.server.commands;

import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.IDroolsSession;

public interface IStatefulSessionCommand extends IBPMCommand<IDroolsSession> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 * @param service
	 */
	void execute(IDroolsSession target, IBPMService service);
}
