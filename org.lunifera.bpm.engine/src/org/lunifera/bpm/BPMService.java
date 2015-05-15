package org.lunifera.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.KnowledgeBaseFactoryService;
import org.drools.base.MapGlobalResolver;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactoryService;
import org.drools.io.ResourceFactoryService;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.process.workitem.wsht.CommandBasedWSHumanTaskHandler;
import org.jbpm.process.workitem.wsht.MinaHTWorkItemHandler;
import org.jbpm.task.service.TaskService;
import org.lunifera.bpm.api.IBPMService;
import org.lunifera.bpm.api.IKnowledgeBaseCommand;
import org.lunifera.bpm.api.IStatefulSessionCommand;
import org.lunifera.bpm.api.ITaskServiceCommand;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class BPMService implements IBPMService {
	private static final String JNDI_USER_TRANSACTION = "osgi:service/javax.transaction.UserTransaction";

	private final static Logger LOGGER = LoggerFactory
			.getLogger(BPMService.class);

	private ComponentContext context;

	private KnowledgeBuilderFactoryService knowledgeBuilderFactoryService;
	private KnowledgeBaseFactoryService knowledgeBaseFactoryService;
	private KnowledgeBaseConfiguration kbaseConf;
	private KnowledgeBuilder kbuilder;
	private KnowledgeSessionConfiguration kieSessionConfig;
	private Environment env;
	private ResourceFactoryService resourceFactoryService;
	private KnowledgeBuilderConfiguration kbConf;
	private KnowledgeBase kbase;
	private TaskService taskService;
	private TransactionManager tm;
	private EntityManagerFactory emf;

	@Activate
	protected void activate(ComponentContext context) {
		LOGGER.debug("BPMEngine activated");

		this.context = context;

		kbaseConf = knowledgeBaseFactoryService.newKnowledgeBaseConfiguration(
				null, getClass().getClassLoader());
		kbConf = knowledgeBuilderFactoryService
				.newKnowledgeBuilderConfiguration(null, getClass()
						.getClassLoader());
		kbuilder = knowledgeBuilderFactoryService.newKnowledgeBuilder(kbConf);
		if (kbuilder == null) {
			throw new RuntimeException("Knowledgebase initialization failed");
		}
		kbase = knowledgeBaseFactoryService.newKnowledgeBase(kbaseConf);

		Properties properties = new Properties();
		properties
				.put("drools.processInstanceManagerFactory",
						"org.jbpm.persistence.processinstance.JPAProcessInstanceManagerFactory");
		properties.put("drools.processSignalManagerFactory",
				"org.jbpm.persistence.processinstance.JPASignalManagerFactory");

		// supply bundle classloader to drools
		Collection<ClassLoader> classLoaders = new ArrayList<ClassLoader>();
		classLoaders.add(getClass().getClassLoader());

		env = KnowledgeBaseFactory.newEnvironment();
		env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
		env.set(EnvironmentName.TRANSACTION_MANAGER, tm);
		env.set(EnvironmentName.TRANSACTION, tm);
		env.set(EnvironmentName.TRANSACTION_SYNCHRONIZATION_REGISTRY, tm);
		env.set(EnvironmentName.GLOBALS, new MapGlobalResolver());

		System.setProperty("jbpm.ut.jndi.lookup", JNDI_USER_TRANSACTION);

		kieSessionConfig = KnowledgeBaseFactory
				.newKnowledgeSessionConfiguration(properties);
	}

	@Deactivate
	protected void deactivate(ComponentContext context) {
		this.context = null;
	}

	@Reference(name = "entityManagerFactory", cardinality = ReferenceCardinality.MANDATORY, target = "(osgi.unit.name=org.lunifera.jbpm.persistence.jpa)", unbind = "unbindEntityManagerFactory")
	protected synchronized void bindEntityManagerFactory(
			EntityManagerFactory emf) {
		this.emf = emf;
	}

	protected synchronized void unbindEntityManagerFactory(
			EntityManagerFactory emf) {
		this.emf = null;
	}

	@Reference(name = "transactionManager", cardinality = ReferenceCardinality.MANDATORY, unbind = "unbindTransactionManager")
	protected synchronized void bindTransactionManager(TransactionManager tm) {
		this.tm = tm;
	}

	protected synchronized void unbindTransactionManager(TransactionManager tm) {
		this.tm = null;
	}

	@Reference(name = "knowledgeBuilderFactoryService", cardinality = ReferenceCardinality.MANDATORY, unbind = "unbindKnowledgeBuilderFactoryService")
	protected synchronized void bindKnowledgeBuilderFactoryService(
			KnowledgeBuilderFactoryService knowledgeBuilderFactoryService) {
		this.knowledgeBuilderFactoryService = knowledgeBuilderFactoryService;
	}

	protected synchronized void unbindKnowledgeBuilderFactoryService(
			KnowledgeBuilderFactoryService knowledgeBuilderFactoryService) {
		this.knowledgeBuilderFactoryService = null;
	}

	@Reference(name = "resourceFactoryService", cardinality = ReferenceCardinality.MANDATORY, unbind = "unbindResourceFactoryService")
	protected synchronized void bindResourceFactoryService(
			ResourceFactoryService resourceFactoryService) {
		this.resourceFactoryService = resourceFactoryService;
	}

	protected synchronized void unbindResourceFactoryService(
			ResourceFactoryService resourceFactoryService) {
		this.resourceFactoryService = null;
	}

	@Reference(name = "knowledgeBaseFactoryService", cardinality = ReferenceCardinality.MANDATORY, unbind = "unbindKnowledgeBaseFactoryService")
	protected synchronized void bindKnowledgeBaseFactoryService(
			KnowledgeBaseFactoryService knowledgeBaseFactoryService) {
		this.knowledgeBaseFactoryService = knowledgeBaseFactoryService;
	}

	protected synchronized void unbindKnowledgeBaseFactoryService(
			KnowledgeBaseFactoryService knowledgeBaseFactoryService) {
		this.knowledgeBaseFactoryService = null;
	}

	public ResourceFactoryService getResourceFactoryService() {
		return resourceFactoryService;
	}

	@Override
	public KnowledgeBuilder getKnowledgeBuilder() {
		return kbuilder;
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		return kbase;
	}

	@Override
	public TaskService getTaskService() {
		return taskService;
	}

	@Override
	public void startProcess(String processId, Map<String, Object> parameters,
			boolean loggerOn) {

		LOGGER.debug("starting process " + processId);
		StatefulKnowledgeSession ksession = createSession();

		KnowledgeRuntimeLogger logger = null;
		if (loggerOn) {
			logger = KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(
					ksession, processId, 1000);
		}

		LOGGER.debug(processId + " started ");
		ksession.startProcess(processId, parameters);
		ksession.fireAllRules();

		if (loggerOn) {
			logger.close();
		}

		ksession.dispose();

		LOGGER.debug("all rules were fired for " + processId);
	}

	@SuppressWarnings("deprecation")
	protected StatefulKnowledgeSession createSession() {
		StatefulKnowledgeSession ksession;
		// create a new knowledge session that uses JPA to store the runtime
		// state
		ksession = JPAKnowledgeService.newStatefulKnowledgeSession(kbase,
				kieSessionConfig, env);

		ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
				new MinaHTWorkItemHandler(ksession));

		CommandBasedWSHumanTaskHandler taskHandler = new CommandBasedWSHumanTaskHandler(
				ksession);
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
				taskHandler);
		taskHandler.connect();
		return ksession;
	}

	private boolean isActive() {
		return context != null;
	}

	@Override
	public void execute(ITaskServiceCommand command) {
		if (isActive() && command != null) {
			command.execute(taskService, this);
		}
	}

	@Override
	public void execute(IKnowledgeBaseCommand command) {
		if (isActive() && command != null) {
			command.execute(kbase, this);
		}
	}

	@Override
	public void execute(IStatefulSessionCommand command) {
		if (isActive() && command != null) {
			StatefulKnowledgeSession session = createSession();
			try {
				command.execute(session, this);
			} finally {
				session.dispose();
			}
		}
	}
}
