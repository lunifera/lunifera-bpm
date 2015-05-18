package org.lunifera.bpm.drools.ui.vaadin.container;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.IDroolsSession;
import org.lunifera.bpm.drools.common.server.commands.IStatefulSessionCommand;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItemContainer;

@SuppressWarnings("serial")
public class ProcessInstanceContainer extends
		DeepResolvingBeanItemContainer<ProcessInstance> {

	public ProcessInstanceContainer(IBPMService bpmService)
			throws IllegalArgumentException {
		super(ProcessInstance.class);

		IStatefulSessionCommand command = new IStatefulSessionCommand() {
			@Override
			public void execute(IDroolsSession session, IBPMService service) {
				StatefulKnowledgeSession ksession = session.getWrappedSession();
				ProcessInstance instance = ksession.getProcessInstance(1);
				instance.getProcess();
				addBean(instance);
			}
		};
		bpmService.execute(command);
	}
}
