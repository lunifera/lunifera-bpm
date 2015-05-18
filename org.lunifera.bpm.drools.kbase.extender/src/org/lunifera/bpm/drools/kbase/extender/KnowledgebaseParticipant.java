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
package org.lunifera.bpm.drools.kbase.extender;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.eclipse.emf.common.util.URI;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.commands.AddToKnowledgeBaseCommand;
import org.lunifera.xtext.builder.metadata.services.AbstractBuilderParticipant;
import org.lunifera.xtext.builder.metadata.services.IBuilderParticipant;
import org.lunifera.xtext.builder.metadata.services.IMetadataBuilderService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.google.inject.Inject;

@Component(service = { IBuilderParticipant.class })
public class KnowledgebaseParticipant extends AbstractBuilderParticipant
		implements BundleListener {

	private static final String LUN_DROOLS_PROVIDER = "Lun-Drools-Provider";
	@Inject
	private IMetadataBuilderService metadataBuilderService;

	private ComponentContext context;
	private IBPMService bpmService;

	public KnowledgebaseParticipant() {

	}

	@Activate
	protected void activate(ComponentContext context) {
		this.context = context;
	}

	@Deactivate
	protected void deactivate(ComponentContext context) {
		metadataBuilderService.removeFromBundleSpace(context.getBundleContext()
				.getBundle());

		this.context = null;
	}

	@Reference(name = "bpmService", cardinality = ReferenceCardinality.MANDATORY, unbind = "unbindBpmService")
	protected synchronized void bindBpmService(IBPMService service) {
		this.bpmService = service;
	}

	protected synchronized void unbindBpmService(IBPMService service) {
		this.bpmService = null;
	}

	@Override
	public List<URL> getModels(Bundle suspect) {

		if (!containsHeader(suspect, LUN_DROOLS_PROVIDER)) {
			return Collections.emptyList();
		}

		registerArtifacts(suspect);

		// nothing to return
		return Collections.emptyList();
	}

	/**
	 * Returns true, if the bundle contains the header.
	 * 
	 * @param bundle
	 * @param header
	 * @return
	 */
	private boolean containsHeader(Bundle bundle, String header) {
		Dictionary<String, String> headers = bundle.getHeaders();
		Enumeration<String> keys = headers.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key.equals(header)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Register transations at the i18n registry
	 * 
	 * @param bundle
	 */
	private void registerArtifacts(Bundle bundle) {

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();

		Map<ResourceType, List<URL>> results = internalFindURLs(bundle);
		for (Entry<ResourceType, List<URL>> entry : results.entrySet()) {
			for (URL url : entry.getValue()) {
				kbuilder.add(ResourceFactory.newUrlResource(url),
						entry.getKey());
			}
		}

		AddToKnowledgeBaseCommand command = new AddToKnowledgeBaseCommand(
				kbuilder);
		bpmService.execute(command);
	}

	/**
	 * Searches for all I18n translations in the given bundle.
	 * 
	 * @param bundle
	 * @return
	 */
	private Map<ResourceType, List<URL>> internalFindURLs(Bundle bundle) {

		BundleWiring wiring = bundle.adapt(BundleWiring.class);

		Map<ResourceType, List<URL>> results = new HashMap<ResourceType, List<URL>>();
		collectURLs(ResourceType.BPMN2, results, wiring);
		collectURLs(ResourceType.BRL, results, wiring);
		collectURLs(ResourceType.CHANGE_SET, results, wiring);
		collectURLs(ResourceType.DESCR, results, wiring);
		collectURLs(ResourceType.DRF, results, wiring);
		collectURLs(ResourceType.DRL, results, wiring);
		collectURLs(ResourceType.DSL, results, wiring);
		collectURLs(ResourceType.DSLR, results, wiring);
		collectURLs(ResourceType.DTABLE, results, wiring);
		collectURLs(ResourceType.PKG, results, wiring);
		collectURLs(ResourceType.PMML, results, wiring);
		collectURLs(ResourceType.XDRL, results, wiring);
		collectURLs(ResourceType.XSD, results, wiring);

		Set<String> fragments = new HashSet<String>();
		for (Map.Entry<ResourceType, List<URL>> entry : results.entrySet()) {
			for (Iterator<URL> iterator = entry.getValue().iterator(); iterator
					.hasNext();) {
				URL url = iterator.next();

				URI uri = URI.createURI(url.toString());
				if (fragments.contains(uri.lastSegment())) {
					iterator.remove();
				}
				fragments.add(uri.lastSegment());
			}
		}
		return results;
	}

	protected void collectURLs(ResourceType type,
			Map<ResourceType, List<URL>> results, BundleWiring wiring) {
		List<URL> temp = new ArrayList<URL>();
		temp.addAll(wiring.findEntries("/bpm/",
				ext(type.getDefaultExtension()),
				BundleWiring.LISTRESOURCES_RECURSE));
		results.put(type, temp);
	}

	private String ext(String ext) {
		return "*." + ext;
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		if (event.getType() == BundleEvent.RESOLVED) {
			registerArtifacts(event.getBundle());
		} else if (event.getType() == BundleEvent.UNINSTALLED) {
			// TODO implement me later
		}
	}

	@Override
	public void notifyLifecyle(LifecycleEvent event) {
		if (event.getState() == IBuilderParticipant.LifecycleEvent.INITIALIZE) {
		} else if (event.getState() == IBuilderParticipant.LifecycleEvent.ACTIVATED) {
			context.getBundleContext().addBundleListener(this);

		} else if (event.getState() == IBuilderParticipant.LifecycleEvent.DEACTIVATED) {
			// do not remove the bundle listener! Otherwise the changes will not
			// be tracked
		}
	}

}
