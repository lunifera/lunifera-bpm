package org.lunifera.bpm.drools.common.server.commands;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddToKnowledgeBaseCommand implements IKnowledgeBaseCommand {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(AddToKnowledgeBaseCommand.class);

	private KnowledgeBuilder kbuilder;

	public AddToKnowledgeBaseCommand(KnowledgeBuilder kbuilder) {
		super();
		this.kbuilder = kbuilder;
	}

	@Override
	public void execute(KnowledgeBase kBase) {

		kBase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		LOGGER.debug("Added knowledge packages to kBase ");
	}

}
