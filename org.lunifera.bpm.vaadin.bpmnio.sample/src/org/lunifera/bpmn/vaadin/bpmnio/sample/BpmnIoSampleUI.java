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
package org.lunifera.bpmn.vaadin.bpmnio.sample;

import org.lunifera.bpmn.vaadin.bpmnio.BpmnModeler;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
@Theme(Reindeer.THEME_NAME)
@Push
public class BpmnIoSampleUI extends UI {

	protected BpmnModeler modler;

	@Override
	protected void init(VaadinRequest request) {

		final VerticalLayout content = new VerticalLayout();
		Button b = new Button("Create new modeler");
		content.addComponent(b);
		setContent(content);

		b.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				modler = new BpmnModeler();
				modler.setSizeFull();
				content.addComponent(modler);
			}
		});

		Button b2 = new Button("Click");
		content.addComponent(b2);
		b2.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
//				modler.click();
			}
		});
	}
}
