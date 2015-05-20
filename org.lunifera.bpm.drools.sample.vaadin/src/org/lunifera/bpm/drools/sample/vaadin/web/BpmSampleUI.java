package org.lunifera.bpm.drools.sample.vaadin.web;

import java.net.URL;

import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.IDroolsSession;
import org.lunifera.bpm.drools.sample.vaadin.Activator;
import org.lunifera.bpm.drools.ui.vaadin.ProcessField;
import org.lunifera.bpm.drools.ui.vaadin.TaskField;
import org.lunifera.ecview.core.common.context.URLI18nService;
import org.lunifera.runtime.web.vaadin.common.resource.impl.ThemeResourceProvider;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
@Theme(Reindeer.THEME_NAME)
@Push
public class BpmSampleUI extends UI {

	private ServiceReference<IBPMService> ref;
	private IBPMService bpmService;
	private ProcessField processField;
	private TaskField taskField;
	private URLI18nService i18nService;
	private IDroolsSession session;

	@Override
	protected void init(VaadinRequest request) {

		setupI18n();

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		setContent(layout);
		layout.setStyleName(Reindeer.LAYOUT_BLUE);
		layout.setMargin(true);
		layout.setSpacing(true);

		// Activate the realm
		VaadinObservables.activateRealm(this);

		getBpmService();

		processField = new ProcessField(bpmService, i18nService,
				new ThemeResourceProvider());
		layout.addComponent(processField);
		processField.setSizeFull();

		taskField = new TaskField(i18nService, new ThemeResourceProvider());
		layout.addComponent(taskField);
		taskField.setSizeFull();

	}

	protected void setupI18n() {
		i18nService = new URLI18nService();

		BundleContext context = FrameworkUtil.getBundle(getClass())
				.getBundleContext();
		for (Bundle bundle : context.getBundles()) {
			if (bundle.getSymbolicName().equals(
					"org.lunifera.bpm.drools.ui.vaadin")) {
				URL entry = bundle.getEntry("/i18n/i18n.properties");
				if (entry != null) {
					i18nService.append(entry);
				}
				break;
			}
		}
	}

	protected void getBpmService() {
		ref = Activator.getContext().getServiceReference(IBPMService.class);
		if (ref != null) {
			bpmService = Activator.getContext().getService(ref);
		}
	}

	@Override
	public void close() {
		bpmService = null;
		Activator.getContext().ungetService(ref);
		ref = null;

		session.dispose();
		session = null;

		super.close();
	}

}
