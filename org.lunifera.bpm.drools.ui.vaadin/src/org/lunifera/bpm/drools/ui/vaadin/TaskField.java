package org.lunifera.bpm.drools.ui.vaadin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItemContainer;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TaskField extends AbstractBPMField<TaskSummary> {

	public static final String EVENT_SELECTED_TASK = "lunifera/task/selected";

	private final TaskService taskService;
	private DeepResolvingBeanItemContainer<TaskSummary> container;
	private VerticalLayout mainLayout;
	private HorizontalLayout buttonBar;
	private Button claimButton;
	private Button activateButton;
	private Button startButton;
	private Button completeButton;
	private Button resumeButton;
	private Button refreshTableButton;

	public TaskField(TaskService taskService, II18nService i18nService,
			IResourceProvider resourceProvider) {
		this(taskService, i18nService, resourceProvider, true);
	}

	public TaskField(TaskService taskService, II18nService i18nService,
			IResourceProvider resourceProvider, boolean createButtonbar) {
		super(i18nService, resourceProvider, createButtonbar);
		this.taskService = taskService;
	}

	@Override
	protected Component initContent() {

		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		if (createButtonBar) {
			createButtonBar();
		}

		table = new Table();
		table.setImmediate(true);
		table.setSelectable(true);
		table.setNullSelectionAllowed(true);
		table.setSizeFull();
		container = new DeepResolvingBeanItemContainer<TaskSummary>(
				TaskSummary.class);
		table.setContainerDataSource(container);
		mainLayout.addComponent(table);

		applyColumns(Arrays.asList("name", "subject", "description", "status",
				"priority", "skipable", "actualOwner.id", "createdBy.id",
				"createdOn", "id"));

		table.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				TaskField.this.setValue((TaskSummary) table.getValue(), false);
			}
		});

		mainLayout.setExpandRatio(table, 1.0f);

		loadTasks();

		return mainLayout;
	}

	protected void createButtonBar() {
		buttonBar = new HorizontalLayout();
		buttonBar.setSizeUndefined();
		buttonBar.setSpacing(true);
		mainLayout.addComponent(buttonBar);

		claimButton = new Button();
		claimButton.setCaption(translate(toFqnColumnId("claim")));
		buttonBar.addComponent(claimButton);
		claimButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (getValue() != null) {
					claim(getUserId());
				}
			}
		});

		activateButton = new Button();
		activateButton.setCaption(translate(toFqnColumnId("activate")));
		buttonBar.addComponent(activateButton);
		activateButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (getValue() != null) {
					activate(getUserId());
				}
			}
		});

		startButton = new Button();
		startButton.setCaption(translate(toFqnColumnId("start")));
		buttonBar.addComponent(startButton);
		startButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (getValue() != null) {
					start(getUserId());
				}
			}
		});

		completeButton = new Button();
		completeButton.setCaption(translate(toFqnColumnId("complete")));
		buttonBar.addComponent(completeButton);
		completeButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (getValue() != null) {
					complete(getUserId(), null);
				}
			}
		});

		resumeButton = new Button();
		resumeButton.setCaption(translate(toFqnColumnId("resume")));
		buttonBar.addComponent(resumeButton);
		resumeButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (getValue() != null) {
					resume(getUserId());
				}
			}
		});

		refreshTableButton = new Button();
		refreshTableButton.setCaption(translate(toFqnColumnId("refreshTable")));
		buttonBar.addComponent(refreshTableButton);
		refreshTableButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				refresh();
			}
		});
	}

	protected String getUserId() {
		return "florian";
	}

	@Override
	public void detach() {
		try {
			taskService.disconnect();
		} catch (Exception e) {
		}

		super.detach();
	}

	protected void loadTasks() {
		container.removeAllItems();

		List<TaskSummary> tasks = taskService
				.getTasksAssignedAsBusinessAdministrator("Administrator",
						"en-UK");

		container.addAll(tasks);
	}

	@Override
	public Class<TaskSummary> getType() {
		return TaskSummary.class;
	}

	protected String toFqnColumnId(String column) {
		return "org.lunifera.bpm.ui.vaadin.Tasks." + column;
	}

	@Override
	protected boolean isCollapsed(String fqnColumnId) {
		if (fqnColumnId.equals(toFqnColumnId("id"))) {
			return true;
		}
		return super.isCollapsed(fqnColumnId);
	}

	public void refresh() {
		loadTasks();
	}

	public void activate(String userId) {
		taskService.activate(getValue().getId(), userId);

		refresh();
	}

	public void claim(String userId) {
		taskService.claim(getValue().getId(), userId);

		refresh();
	}

	public void start(String userId) {
		taskService.start(getValue().getId(), userId);

		refresh();
	}

	public void complete(String userId, HashMap<String, Object> data) {
		taskService.complete(getValue().getId(), userId, null);

		refresh();
	}

	public void resume(String userId) {
		taskService.resume(getValue().getId(), userId);

		refresh();
	}
}
