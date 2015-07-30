package org.lunifera.bpmn.vaadin.bpmnio.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

/**
 * Client bundle for embedding the wrapper JS api and associated style sheets
 */
public interface BpmnIoClientBundle extends ClientBundle {

	@Source("index.js")
	TextResource javaScript();

	@Source("css/app.css")
	@CssResource.NotStrict
	CssResource css();

	@Source("css/diagram-js.css")
	@CssResource.NotStrict
	CssResource cssOther();
}
