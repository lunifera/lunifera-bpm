package org.lunifera.bpm.drools.ui.vaadin;

import java.util.Arrays;

import org.drools.runtime.process.ProcessInstance;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.ui.vaadin.container.ProcessInstanceContainer;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ProcessInstanceField extends AbstractBPMField<ProcessInstance> {

	private ProcessInstanceContainer container;
	private VerticalLayout mainLayout;

	public ProcessInstanceField(IBPMService bpmService,
			II18nService i18nService, IResourceProvider resourceProvider) {
		super(bpmService, i18nService, resourceProvider);
	}

	@Override
	protected Component initContent() {

		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		table = new Table();
		table.setImmediate(true);
		table.setSelectable(true);
		table.setNullSelectionAllowed(true);
		table.setSizeFull();
		container = new ProcessInstanceContainer(bpmService);
		table.setContainerDataSource(container);

		mainLayout.addComponent(table);

		applyColumns(Arrays.asList("processName", "state", "processId", "id"));

		table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				ProcessInstance itemId = (ProcessInstance) event.getItemId();
				ProcessInstanceField.this.setInternalValue(itemId);
			}
		});

		return mainLayout;
	}

	@Override
	public Class<ProcessInstance> getType() {
		return ProcessInstance.class;
	}

	protected String toFqnColumnId(String column) {
		return "org.lunifera.bpm.ui.vaadin.ProcessInstance." + column;
	}

	@Override
	protected boolean isCollapsed(String fqnColumnId) {
		if (fqnColumnId.equals(toFqnColumnId("id"))) {
			return true;
		}
		return super.isCollapsed(fqnColumnId);
	}

}
