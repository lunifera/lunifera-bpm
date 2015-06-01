package org.lunifera.bpm.drools.common.server;

import org.drools.KnowledgeBase;
import org.drools.command.Command;
import org.drools.io.ResourceFactoryService;
import org.lunifera.bpm.drools.common.server.commands.IKnowledgeBaseCommand;
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
	 * @param disposeSession
	 *            true, if the created session should be disposed
	 */
	public <M> M execute(final Command<M> command, boolean disposeSession);

	/**
	 * Creates a new drools session.
	 * 
	 * @return
	 */
	IDroolsSession createSession();

	/**
	 * Creates a new task client.
	 * 
	 * @return
	 */
	public org.jbpm.task.TaskService createTaskClient();

	/**
	 * Loads the session with the given id.
	 * 
	 * @param sessionId
	 * @return
	 */
	IDroolsSession loadSession(int sessionId);

}
