package org.lunifera.bpm.api;

public interface IBPMCommand<M> {

	/**
	 * Is executed by the bpm service.
	 * 
	 * @param target
	 * @param service
	 */
	void execute(M target, IBPMService service);
}
