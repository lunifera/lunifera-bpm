package org.lunifera.bpm.drools.common.server;

import org.drools.runtime.StatefulKnowledgeSession;
import org.lunifera.runtime.common.dispose.IDisposable;

public interface IDroolsSession extends IDisposable {

	StatefulKnowledgeSession getWrappedSession();

	void registerDisposal(DroolsDisposeable disposable);

	interface DroolsDisposeable {

		void dispose();

	}

}
