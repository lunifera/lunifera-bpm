package org.lunifera.bpm.drools.common.server.commands;

import java.util.Map;

import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.IDroolsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartProcessCommand implements IStatefulSessionCommand {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(StartProcessCommand.class);
 
	private String processId;
	private Map<String, Object> parameters;
	private boolean loggerOn;

	public StartProcessCommand(String processId,
			Map<String, Object> parameters, boolean loggerOn) {
		super();
		this.processId = processId;
		this.parameters = parameters;
		this.loggerOn = loggerOn;
	}

	@Override
	public void execute(IDroolsSession session, IBPMService service) {

		StatefulKnowledgeSession ksession = session.getWrappedSession();
		
		LOGGER.debug("starting process " + processId);

		KnowledgeRuntimeLogger logger = null;
		if (loggerOn) {
			logger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(
					ksession, processId, 1000);
		}

		LOGGER.debug(processId + " started ");
		ksession.startProcess(processId, parameters);
		ksession.fireAllRules();

		if (logger != null) {
			logger.close();
		}

		LOGGER.debug("all rules were fired for " + processId);
	}

}
