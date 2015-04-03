package org.lunifera.bpmn.vaadin.bpmnio.client.resources;

import com.google.gwt.core.client.GWT;

/**
 * Injector for the JavaScript library and associated CSS files
 */
public class ResourceInjector {
    protected static BpmnIoClientBundle bundle;

    public static void ensureInjected() {
        if (bundle == null) {
            bundle = GWT.create(BpmnIoClientBundle.class);
            ResourceInjector injector = GWT
                    .create(ResourceInjector.class);
            injector.injectResources();
        }
    }

    /**
     * Override this with deferred binding to customize injected stuff
     */
    protected void injectResources() {
        bundle.css().ensureInjected();
        bundle.cssOther().ensureInjected();
        injectScript(bundle.javaScript().getText());
    }

    private static native void injectScript(String script)
	/*-{
        $wnd.eval(script);
    }-*/;
}
