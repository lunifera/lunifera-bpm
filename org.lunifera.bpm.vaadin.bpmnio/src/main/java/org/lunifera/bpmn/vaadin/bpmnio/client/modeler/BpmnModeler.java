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

import com.google.gwt.core.client.JavaScriptObject;

public class BpmnModeler extends JavaScriptObject {

	protected BpmnModeler() {

	}

	public static native BpmnModeler create()
	/*-{
	 	var $ = require('jquery'),
		BpmnModeler = require('bpmn-js/lib/Modeler');

		var container = $('#js-drop-zone');

		var canvas = $('#bpmnio-canvas');
		var renderer = new BpmnModeler({ container: canvas });
		var newDiagramXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<bpmn2:definitions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\" id=\"sample-diagram\" targetNamespace=\"http://bpmn.io/schema/bpmn\">\n  <bpmn2:process id=\"Process_1\" isExecutable=\"false\">\n    <bpmn2:startEvent id=\"StartEvent_1\"/>\n  </bpmn2:process>\n  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Process_1\">\n      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n        <dc:Bounds height=\"36.0\" width=\"36.0\" x=\"412.0\" y=\"240.0\"/>\n      </bpmndi:BPMNShape>\n    </bpmndi:BPMNPlane>\n  </bpmndi:BPMNDiagram>\n</bpmn2:definitions>";

		$wnd.BPMModeler = {}
		$wnd.BPMModeler.renderer = renderer
		$wnd.BPMModeler.newDiagramXML = newDiagramXML
		
		return {};
	}-*/;

	public native final void clicked()
	/*-{
	   console.log("Clicked");
	}-*/;

	// public native final void createNewDiagram()
	// /*-{
	// var newDiagramXML = fs.readFileSync(__dirname +
	// '/../resources/newDiagram.bpmn', 'utf-8');
	// openDiagram(newDiagramXML);
	// }-*/;
	//
	// public native final void openDiagramFromUrl(String url)
	// /*-{
	// var newDiagramXML = fs.readFileSync(url, 'utf-8');
	// openDiagramFromXml(newDiagramXML);
	// }-*/;
	//
	// public native final void openDiagramFromName(String resourceName)
	// /*-{
	// var newDiagramXML = fs.readFileSync(__dirname + '/../resources/' +
	// resourceName, 'utf-8');
	// openDiagramFromXml(newDiagramXML);
	// }-*/;
	//
	// public native final void openDiagramFromXml(String xml)
	// /*-{
	// renderer.importXML(xml, function(err) {
	// if (err) {
	// container
	// .removeClass('with-diagram')
	// .addClass('with-error');
	//
	// container.find('.error pre').text(err.message);
	// console.error(err);
	// } else {
	// container
	// .removeClass('with-error')
	// .addClass('with-diagram');
	// }
	// });
	// }-*/;

}
