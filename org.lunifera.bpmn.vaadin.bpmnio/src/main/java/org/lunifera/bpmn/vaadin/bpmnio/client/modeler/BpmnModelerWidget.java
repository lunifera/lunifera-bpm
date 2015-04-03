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

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class BpmnModelerWidget extends Widget {

	private BpmnModelerJS bpmEditor;

	public BpmnModelerWidget() {
		setElement(Document.get().createDivElement());
		setStylePrimaryName(getElement(), "bpmn-io-editor");
	}

	public void initEditor() {
		bpmEditor = BpmnModelerJS.create();
	}

	public boolean isEditorInitialized() {
		return bpmEditor != null;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		initEditor();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}

	public BpmnModelerJS getBpmnEditor() {
		return bpmEditor;
	}

	public void setBpmnUrl(String url) {
		getBpmnEditor().openDiagramFromUrl(url);
	}
	
}