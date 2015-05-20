package org.lunifera.bpm.drools.ui.vaadin;

import java.util.Arrays;
import java.util.HashMap;

import org.drools.command.runtime.process.StartProcessCommand;
import org.drools.definition.process.Process;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.ui.vaadin.container.ProcessContainer;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ProcessField extends
		AbstractBPMField<org.drools.definition.process.Process> {

	private final IBPMService bpmService;
	private ProcessContainer container;
	private VerticalLayout mainLayout;
	private HorizontalLayout buttonBar;
	private Button startButton;
	private Button refreshTableButton;

	public ProcessField(IBPMService bpmService, II18nService i18nService,
			IResourceProvider resourceProvider) {
		this(bpmService, i18nService, resourceProvider, true);
	}

	public ProcessField(IBPMService bpmService, II18nService i18nService,
			IResourceProvider resourceProvider, boolean createButtonBar) {
		super(i18nService, resourceProvider, createButtonBar);
		this.bpmService = bpmService;
	}

	@Override
	protected Component initContent() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		if (createButtonBar) {
			createButtonBar();
		}

		table = new Table();
		table.setSizeFull();
		table.setImmediate(true);
		table.setSelectable(true);
		table.setNullSelectionAllowed(true);
		table.setSizeFull();
		container = new ProcessContainer(bpmService.getKnowledgeBase());
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

		mainLayout.setExpandRatio(table, 1.0f);

		return mainLayout;
	}

	protected void createButtonBar() {
		buttonBar = new HorizontalLayout();
		buttonBar.setSizeUndefined();
		buttonBar.setSpacing(true);
		mainLayout.addComponent(buttonBar);

		startButton = new Button();
		startButton.setCaption(translate(toFqnColumnId("start")));
		buttonBar.addComponent(startButton);
		startButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (getValue() != null) {
					startProcess(null);
				}
			}
		});

		refreshTableButton = new Button();
		refreshTableButton.setCaption(translate(toFqnColumnId("refreshTable")));
		buttonBar.addComponent(refreshTableButton);
		refreshTableButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				container.reload();
			}
		});
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
				.getId(), data, null);
		bpmService.execute(command, true);
	}

}
