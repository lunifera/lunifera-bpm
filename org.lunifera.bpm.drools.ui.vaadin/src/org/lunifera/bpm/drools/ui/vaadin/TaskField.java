package org.lunifera.bpm.drools.ui.vaadin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.drools.SystemEventListenerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.SyncTaskServiceWrapper;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.mina.MinaTaskClientConnector;
import org.jbpm.task.service.mina.MinaTaskClientHandler;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItemContainer;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TaskField extends AbstractBPMField<TaskSummary> {

	private DeepResolvingBeanItemContainer<TaskSummary> container;
	private VerticalLayout mainLayout;
	private TaskService client;
	private StatefulKnowledgeSession ksession;
	private HorizontalLayout buttonBar;
	private Button claimButton;
	private Button activateButton;
	private Button startButton;
	private Button completeButton;
	private Button resumeButton;
	private Button refreshTableButton;

	public TaskField(II18nService i18nService,
			IResourceProvider resourceProvider) {
		this(i18nService, resourceProvider, true);
	}

	public TaskField(II18nService i18nService,
			IResourceProvider resourceProvider, boolean createButtonbar) {
		super(i18nService, resourceProvider, createButtonbar);
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

		// ServiceReference<EntityManagerFactory> ref = FrameworkUtil
		// .getBundle(getClass()).getBundleContext()
		// .getServiceReference(EntityManagerFactory.class);
		// EntityManagerFactory emf = FrameworkUtil.getBundle(getClass())
		// .getBundleContext().getService(ref);

		// client = new LocalTaskService(new org.jbpm.task.service.TaskService(
		// emf, SystemEventListenerFactory.getSystemEventListener()));
		// LocalHTWorkItemHandler handler = new LocalHTWorkItemHandler(client,
		// ksession, OnErrorAction.RETHROW);
		// handler.connect();
		client = new SyncTaskServiceWrapper(new TaskClient(
				new MinaTaskClientConnector(UUID.randomUUID().toString(),
						new MinaTaskClientHandler(
								SystemEventListenerFactory
										.getSystemEventListener()))));
		client.connect();

		table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				setInternalValue((TaskSummary) event.getItemId());
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
			client.disconnect();
		} catch (Exception e) {
		}

		super.detach();
	}

	protected void loadTasks() {
		container.removeAllItems();

		List<TaskSummary> tasks = client
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
		client.activate(getValue().getId(), userId);

		refresh();
	}

	public void claim(String userId) {
		client.claim(getValue().getId(), userId);

		refresh();
	}

	public void start(String userId) {
		client.start(getValue().getId(), userId);

		refresh();
	}

	public void complete(String userId, HashMap<String, Object> data) {
		client.complete(getValue().getId(), userId, null);

		refresh();
	}

	public void resume(String userId) {
		client.resume(getValue().getId(), userId);

		refresh();
	}
}
