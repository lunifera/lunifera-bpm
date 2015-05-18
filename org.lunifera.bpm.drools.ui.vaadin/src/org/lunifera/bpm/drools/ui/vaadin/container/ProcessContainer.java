package org.lunifera.bpm.drools.ui.vaadin.container;

import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItemContainer;

@SuppressWarnings("serial")
public class ProcessContainer extends
		DeepResolvingBeanItemContainer<org.drools.definition.process.Process> {

	public ProcessContainer(IBPMService bpmService)
			throws IllegalArgumentException {
		super(org.drools.definition.process.Process.class);

		// drools does not support lazy loading of processes. So we add all of
		// them.
		addAll(bpmService.getKnowledgeBase().getProcesses());
	}
}
