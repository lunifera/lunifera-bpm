package org.lunifera.bpm.drools.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.KnowledgeBaseFactoryService;
import org.drools.SystemEventListenerFactory;
import org.drools.base.MapGlobalResolver;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactoryService;
import org.drools.command.Command;
import org.drools.io.ResourceFactoryService;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.task.service.SyncTaskServiceWrapper;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.local.LocalTaskService;
import org.jbpm.task.service.mina.MinaTaskClientConnector;
import org.jbpm.task.service.mina.MinaTaskClientHandler;
import org.lunifera.bpm.drools.common.server.DroolsSession;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.IDroolsSession;
import org.lunifera.bpm.drools.common.server.commands.IKnowledgeBaseCommand;
import org.lunifera.bpm.drools.common.server.commands.ITaskServiceCommand;
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
	private KnowledgeSessionConfiguration kieSessionConfig;
	private Environment env;
	private KnowledgeBase kbase;
	private ResourceFactoryService resourceFactoryService;
	private TransactionManager tm;
	private EntityManagerFactory emf;

	@Activate
	protected void activate(ComponentContext context) {
		LOGGER.debug("BPMEngine activating");

		this.context = context;

		KnowledgeBaseConfiguration kbaseConf = knowledgeBaseFactoryService
				.newKnowledgeBaseConfiguration(null, getClass()
						.getClassLoader());
		KnowledgeBuilderConfiguration kbConf = knowledgeBuilderFactoryService
				.newKnowledgeBuilderConfiguration(null, getClass()
						.getClassLoader());
		KnowledgeBuilder kbuilder = knowledgeBuilderFactoryService
				.newKnowledgeBuilder(kbConf);
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

		LOGGER.debug("BPMEngine activated");
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

	@Reference(name = "mandatoryDatasource", cardinality = ReferenceCardinality.MANDATORY, target = "(osgi.jndi.service.name=DroolsXADs)", unbind = "unbindDatasource")
	protected synchronized void bindDatasource(DataSource ds) {
		// no need -> only to ensure startup order
		// this service is injected as soon as apache.geronimo.transaction
		// registers the wrapped XADatasource
	}

	protected synchronized void unbindDatasource(DataSource ds) {
		// no need -> only to ensure startup order
	}

	public ResourceFactoryService getResourceFactoryService() {
		return resourceFactoryService;
	}

	@Override
	public KnowledgeBase getKnowledgeBase() {
		return kbase;
	}

	public IDroolsSession createSession() {

		// create a new knowledge session that uses JPA to store the runtime
		// state
		StatefulKnowledgeSession ksession = JPAKnowledgeService
				.newStatefulKnowledgeSession(kbase, kieSessionConfig, env);
		DroolsSession kSession = new DroolsSession(ksession);

		LocalTaskService client = new LocalTaskService(
				new org.jbpm.task.service.TaskService(emf,
						SystemEventListenerFactory.getSystemEventListener()));
		client.connect();
		final org.lunifera.bpm.drools.common.tasks.handler.LocalHTWorkItemHandler taskHandler = new org.lunifera.bpm.drools.common.tasks.handler.LocalHTWorkItemHandler(
				client, ksession);
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
				taskHandler);
		ksession.getWorkItemManager().registerWorkItemHandler("HelloMister",
				new WorkItemHandler() {
					@Override
					public void executeWorkItem(WorkItem workItem,
							WorkItemManager manager) {
						System.out.println("Executed");
//						manager.completeWorkItem(workItem.getId(), null);
					}

					@Override
					public void abortWorkItem(WorkItem workItem,
							WorkItemManager manager) {
						System.out.println("Aborted");
					}
				});
		taskHandler.connect();

		kSession.registerDisposal(new IDroolsSession.DroolsDisposeable() {
			@Override
			public void dispose() {
				try {
					taskHandler.dispose();
				} catch (Exception e) {
					LOGGER.warn(e.getMessage());
				}
			}
		});

		return kSession;
	}

	@Override
	public IDroolsSession loadSession(int sessionId) {

		// create a new knowledge session that uses JPA to store the runtime
		// state
		StatefulKnowledgeSession ksession = JPAKnowledgeService
				.loadStatefulKnowledgeSession(sessionId, kbase,
						kieSessionConfig, env);
		DroolsSession kSession = new DroolsSession(ksession);

		LocalTaskService client = new LocalTaskService(
				new org.jbpm.task.service.TaskService(emf,
						SystemEventListenerFactory.getSystemEventListener()));
		client.connect();
		final org.lunifera.bpm.drools.common.tasks.handler.LocalHTWorkItemHandler taskHandler = new org.lunifera.bpm.drools.common.tasks.handler.LocalHTWorkItemHandler(
				client, ksession);
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
				taskHandler);
		ksession.getWorkItemManager().registerWorkItemHandler("",
				new WorkItemHandler() {
					@Override
					public void executeWorkItem(WorkItem workItem,
							WorkItemManager manager) {
						System.out.println("Executed");
						manager.completeWorkItem(workItem.getId(), null);
					}

					@Override
					public void abortWorkItem(WorkItem workItem,
							WorkItemManager manager) {
						System.out.println("Aborted");
					}
				});
		taskHandler.connect();

		kSession.registerDisposal(new IDroolsSession.DroolsDisposeable() {
			@Override
			public void dispose() {
				try {
					taskHandler.dispose();
				} catch (Exception e) {
					LOGGER.warn(e.getMessage());
				}
			}
		});

		return kSession;

	}

	private boolean isActive() {
		return context != null;
	}

	@Override
	public void execute(ITaskServiceCommand command) {
		if (isActive() && command != null) {
			TaskService taskService = new TaskService(emf,
					SystemEventListenerFactory.getSystemEventListener());
			command.execute(taskService);
		}
	}

	@Override
	public void execute(IKnowledgeBaseCommand command) {
		if (isActive() && command != null) {
			command.execute(kbase);
		}
	}

	@Override
	public <M> M execute(final Command<M> command, boolean disposeSession) {
		if (isActive() && command != null) {
			// create a new session
			final IDroolsSession session = createSession();
			try {
				return session.getWrappedSession().execute(command);
			} finally {
				if (disposeSession) {
					session.dispose();
				}
			}
		}
		return null;
	}

	public org.jbpm.task.TaskService createTaskClient() {
		org.jbpm.task.TaskService client = new SyncTaskServiceWrapper(
				new TaskClient(new MinaTaskClientConnector(UUID.randomUUID()
						.toString(), new MinaTaskClientHandler(
						SystemEventListenerFactory.getSystemEventListener()))));
		return client;
	}

}
