package org.lunifera.bpm.drools.ui.vaadin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.drools.SystemEventListenerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.workitem.wsht.LocalHTWorkItemHandler;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.local.LocalTaskService;
import org.jbpm.task.utils.OnErrorAction;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItemContainer;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TaskField extends AbstractBPMField<TaskSummary> {

	private DeepResolvingBeanItemContainer<TaskSummary> container;
	private VerticalLayout mainLayout;
	private TaskService client;
	private TaskService session;
	private StatefulKnowledgeSession ksession;

	public TaskField(IBPMService bpmService, II18nService i18nService,
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
		container = new DeepResolvingBeanItemContainer<TaskSummary>(
				TaskSummary.class);
		table.setContainerDataSource(container);

		mainLayout.addComponent(table);

		applyColumns(Arrays.asList("name", "subject", "description", "status",
				"priority", "skipable", "actualOwner.id", "createdBy.id",
				"createdOn", "id"));

		ServiceReference<EntityManagerFactory> ref = FrameworkUtil
				.getBundle(getClass()).getBundleContext()
				.getServiceReference(EntityManagerFactory.class);
		EntityManagerFactory emf = FrameworkUtil.getBundle(getClass())
				.getBundleContext().getService(ref);

		ksession = bpmService.createSession().getWrappedSession();
		client = new LocalTaskService(new org.jbpm.task.service.TaskService(
				emf, SystemEventListenerFactory.getSystemEventListener()));
		LocalHTWorkItemHandler handler = new LocalHTWorkItemHandler(client, ksession,
				OnErrorAction.RETHROW);
		handler.connect();
		
		loadTasks();
		table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				setInternalValue((TaskSummary) event.getItemId());
				// TaskSummary itemId = (TaskSummary) event.getItemId();
				//
				// BlockingAddTaskResponseHandler handler = new
				// BlockingAddTaskResponseHandler();
				// Task t = new Task();
				// client.addTask(t, null, handler);
				// taskSummaryResponseHandler.waitTillDone(1000);
				//
				// loadTasks(client);
			}
		});

		return mainLayout;
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

	public void claim(String userId) {
		client.activate(getValue().getId(), userId);
	}

	public void start(String userId) {
		client.start(getValue().getId(), userId);
	}

	public void complete(String userId, HashMap<String, Object> data) {
		// ContentData contentData = ContentMarshallerHelper.marshal(data,
		// null);
		client.complete(getValue().getId(), userId, null);
		ksession.fireAllRules();
	}
}
