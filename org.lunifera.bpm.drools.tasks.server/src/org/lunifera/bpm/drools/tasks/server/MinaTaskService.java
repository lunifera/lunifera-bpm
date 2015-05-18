package org.lunifera.bpm.drools.tasks.server;

import javax.persistence.EntityManagerFactory;

import org.drools.SystemEventListenerFactory;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.mina.MinaTaskServer;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(immediate = true)
public class MinaTaskService {

	private EntityManagerFactory emf;
	private MinaTaskServer server;

	@Activate
	protected void activate() {
		System.setProperty("jbpm.ut.jndi.lookup",
				"osgi:service/javax.transaction.TransactionManager");
		TaskService taskService = new TaskService(emf,
				SystemEventListenerFactory.getSystemEventListener());
		server = new MinaTaskServer(taskService);
		Thread thread = new Thread(server);
		thread.start();
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

	@Deactivate
	protected void deactivate() {
		if (server != null && server.isRunning()) {
			server.stop();
		}
		server = null;
	}

	@Reference(name = "bpmService", cardinality = ReferenceCardinality.MANDATORY, unbind = "unbindBpmService")
	protected synchronized void bindBpmService(IBPMService service) {
		// no need -> only to ensure startup order
	}

	protected synchronized void unbindBpmService(IBPMService service) {
		// no need -> only to ensure startup order
	}
}
