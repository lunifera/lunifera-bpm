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
package org.lunifera.bpmn.vaadin.bpmnio;

import org.lunifera.bpmn.vaadin.bpmnio.client.modeler.BpmnModelerServerRpc;
import org.lunifera.bpmn.vaadin.bpmnio.client.modeler.BpmnModelerState;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponent;

@SuppressWarnings("serial")
public class BpmnModeler extends AbstractComponent {
	private BpmnModelerServerRpc rpc = new BpmnModelerServerRpc() {
		private int clickCount = 0;

		public void clicked(MouseEventDetails mouseDetails) {
		}
	};

	public BpmnModeler() {
		registerRpc(rpc);
	}

	@Override
	public BpmnModelerState getState() {
		return (BpmnModelerState) super.getState();
	}
}
