package org.lunifera.bpm.drools.ui.vaadin.container;

import org.drools.KnowledgeBase;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItemContainer;

@SuppressWarnings("serial")
public class ProcessContainer extends
		DeepResolvingBeanItemContainer<org.drools.definition.process.Process> {

	private KnowledgeBase kBase;

	public ProcessContainer(KnowledgeBase kBase)
			throws IllegalArgumentException {
		super(org.drools.definition.process.Process.class);
		this.kBase = kBase;

		reload();
	}

	public void reload() {
		removeAllItems();

		// drools does not support lazy loading of processes. So we add all of
		// them.
		addAll(kBase.getProcesses());
	}
}
