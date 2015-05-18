package org.lunifera.bpm.drools.common.server.commands;

import org.lunifera.bpm.drools.common.server.IBPMService;

public interface IBPMCommand<M> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 * @param service
	 */
	void execute(M target, IBPMService service);
}
