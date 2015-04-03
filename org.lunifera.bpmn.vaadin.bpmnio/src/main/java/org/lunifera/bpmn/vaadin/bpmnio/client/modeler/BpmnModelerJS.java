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

public class BpmnModelerJS extends JavaScriptObject {

	protected BpmnModelerJS() {

	}

	public static native BpmnModelerJS create()
	/*-{
		var fs = require('fs');
		var $ = require('jquery'),
		BpmnModeler = require('bpmn-js/lib/Modeler');

		var container = $('#js-drop-zone');
		var canvas = $('#js-canvas');
		var renderer = new BpmnModeler({ container: canvas });
	    return new $wnd.BpmnEditorJS();
	}-*/;

	public native final void createNewDiagram()
	/*-{
	   var newDiagramXML = fs.readFileSync(__dirname + '/../resources/newDiagram.bpmn', 'utf-8');
	   openDiagram(newDiagramXML);
	}-*/;

	public native final void openDiagramFromUrl(String url)
	/*-{
	    var newDiagramXML = fs.readFileSync(url, 'utf-8');
	    openDiagramFromXml(newDiagramXML);
	}-*/;

	public native final void openDiagramFromName(String resourceName)
	/*-{
	    var newDiagramXML = fs.readFileSync(__dirname + '/../resources/' + resourceName, 'utf-8');
	    openDiagramFromXml(newDiagramXML);
	}-*/;

	public native final void openDiagramFromXml(String xml)
	/*-{
		renderer.importXML(xml, function(err) {
		    if (err) {
		      container
		        .removeClass('with-diagram')
		        .addClass('with-error');
		
		      container.find('.error pre').text(err.message);
		      console.error(err);
		    } else {
		      container
		        .removeClass('with-error')
		        .addClass('with-diagram');
		    }
		});
	}-*/;

}
