package org.lunifera.bpm.drools.sample.vaadin.web;

import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class ServletService extends VaadinServletService {

	public ServletService(VaadinServlet servlet,
			DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
		super(servlet, deploymentConfiguration);
	}

	@Override
	public ClassLoader getClassLoader() {
		// return the bundle classloader
		// see http://dev.vaadin.com/ticket/15516
		return ServletService.class.getClassLoader();
	}

	public UI findUI(VaadinRequest request) {
		UI instance = super.findUI(request);

		// activate the realm for the current ui and thread
		VaadinObservables.activateRealm(instance);

		return instance;
	}

}
