package org.lunifera.bpm.drools.ui.vaadin;

import java.util.Arrays;
import java.util.HashMap;

import org.drools.definition.process.Process;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.commands.StartProcessCommand;
import org.lunifera.bpm.drools.ui.vaadin.container.ProcessContainer;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ProcessField extends
		AbstractBPMField<org.drools.definition.process.Process> {

	private ProcessContainer container;
	private VerticalLayout mainLayout;

	public ProcessField(IBPMService bpmService, II18nService i18nService,
			IResourceProvider resourceProvider) {
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
		container = new ProcessContainer(bpmService);
		table.setContainerDataSource(container);

		mainLayout.addComponent(table);

		applyColumns(Arrays.asList("name", "version", "packageName", "type",
				"namespace", "id"));

		table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				Process itemId = (Process) event.getItemId();
				ProcessField.this.setInternalValue(itemId);
			}
		});

		return mainLayout;
	}

	@Override
	public Class<org.drools.definition.process.Process> getType() {
		return Process.class;
	}

	protected String toFqnColumnId(String column) {
		return "org.lunifera.bpm.ui.vaadin.Process." + column;
	}

	@Override
	protected boolean isCollapsed(String fqnColumnId) {
		if (fqnColumnId.equals(toFqnColumnId("id"))) {
			return true;
		}
		return super.isCollapsed(fqnColumnId);
	}

	public void startProcess(HashMap<String, Object> data) {
		StartProcessCommand command = new StartProcessCommand(getValue()
				.getId(), data, true);
		bpmService.execute(command);
	}

}
