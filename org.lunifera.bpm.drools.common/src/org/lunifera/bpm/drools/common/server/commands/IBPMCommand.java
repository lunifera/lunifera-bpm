package org.lunifera.bpm.drools.common.server.commands;


public interface IBPMCommand<M> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 */
	void execute(M target);
}
