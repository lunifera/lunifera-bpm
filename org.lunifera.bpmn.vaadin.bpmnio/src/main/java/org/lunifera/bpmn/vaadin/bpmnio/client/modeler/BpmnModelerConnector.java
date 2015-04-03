/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.bpmn.vaadin.bpmnio.client.modeler;

import org.lunifera.bpmn.vaadin.bpmnio.BpmnModeler;
import org.lunifera.bpmn.vaadin.bpmnio.client.resources.ResourceInjector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(BpmnModeler.class)
public class BpmnModelerConnector extends AbstractComponentConnector {

	static{
        ResourceInjector.ensureInjected();
    }
	
	BpmnModelerServerRpc rpc = RpcProxy
			.create(BpmnModelerServerRpc.class, this);

	private String loadedBpm;

	public BpmnModelerConnector() {

	}

	@Override
	protected void init() {
		super.init();
		registerRpc(BpmnModelerClientRpc.class, new BpmnEditorClientRpcImpl());
	}

	@Override
	public BpmnModelerState getState() {
		return (BpmnModelerState) super.getState();
	}

	@Override
	protected Widget createWidget() {
		return GWT.create(BpmnModelerWidget.class);
	}

	@Override
	public BpmnModelerWidget getWidget() {
		return (BpmnModelerWidget) super.getWidget();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		BpmnModelerState state = getState();
		if (state.loadBpmn) {
			String url = getResourceUrl(BpmnModelerState.BPMN_RESOURCE);
			if (loadedBpm == null || !loadedBpm.equals(url)) {
				loadedBpm = url;
				getWidget().setBpmnUrl(url);
			}
		}
	}

	public class BpmnEditorClientRpcImpl implements BpmnModelerClientRpc {

		public void load() {
		}
	}

}
