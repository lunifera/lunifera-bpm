package org.lunifera.bpm.drools.common.server;

import org.drools.KnowledgeBase;
import org.drools.io.ResourceFactoryService;
import org.lunifera.bpm.drools.common.server.commands.IKnowledgeBaseCommand;
import org.lunifera.bpm.drools.common.server.commands.IStatefulSessionCommand;
import org.lunifera.bpm.drools.common.server.commands.ITaskServiceCommand;

/**
 * BpmService. Internally it sets up a proper Drools environment.
 */
public interface IBPMService {

	/**
	 * Returns the knowledge base the server is working with.
	 * 
	 * @return
	 */
	KnowledgeBase getKnowledgeBase();

	/**
	 * Returns the ResourceFactoryService.
	 * 
	 * @return
	 */
	ResourceFactoryService getResourceFactoryService();

	/**
	 * Executes the given command.
	 * 
	 * @param command
	 */
	void execute(ITaskServiceCommand command);

	/**
	 * Executes the given command.
	 * 
	 * @param command
	 */
	void execute(IKnowledgeBaseCommand command);

	/**
	 * Executes the given command.
	 * 
	 * @param command
	 */
	void execute(IStatefulSessionCommand command);

	IDroolsSession createSession();

}
