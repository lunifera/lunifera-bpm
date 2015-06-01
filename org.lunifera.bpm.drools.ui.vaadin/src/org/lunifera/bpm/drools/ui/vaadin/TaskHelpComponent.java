package org.lunifera.bpm.drools.ui.vaadin;

import java.util.Map;

import org.jbpm.task.Task;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.lunifera.runtime.common.event.IEventBroker;
import org.lunifera.runtime.common.help.HelpRequestBuilder;
import org.lunifera.runtime.common.help.IHelpService;
import org.osgi.service.event.EventHandler;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * A component that shows help contents for tasks.
 */
@SuppressWarnings("serial")
public class TaskHelpComponent extends CustomComponent {

	private final IHelpService helpService;

	private VerticalLayout mainLayout;
	private Label content;

	public TaskHelpComponent(IHelpService helpService) {
		this.helpService = helpService;
	}

	public void initialize() {

		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		setCompositionRoot(mainLayout);

		content = new Label("", ContentMode.HTML);
		content.setSizeFull();
		mainLayout.setExpandRatio(content, 1.0f);
		mainLayout.addComponent(content);
	}

	public void setValue(Task task) {
		String html = null;
		if (task != null) {
			Map<String, Object> properties = HelpRequestBuilder.newRequest()
					.type(IHelpService.TYPE__TASK).id(task.getId()).data(task)
					.toRequest();
			html = helpService.getHtml(properties);
		}
		content.setValue(html);
	}
}
