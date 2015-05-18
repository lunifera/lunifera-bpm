package org.lunifera.bpm.drools.common.server;

import java.util.HashSet;
import java.util.Set;

import org.drools.runtime.StatefulKnowledgeSession;
import org.lunifera.runtime.common.dispose.AbstractDisposable;

public class DroolsSession extends AbstractDisposable implements IDroolsSession {

	private final StatefulKnowledgeSession session;
	private Set<DroolsDisposeable> toDispose = new HashSet<>();

	public DroolsSession(StatefulKnowledgeSession session) {
		this.session = session;
	}

	@Override
	protected void internalDispose() {
		session.dispose();

		for (DroolsDisposeable droolsDisposeable : toDispose) {
			droolsDisposeable.dispose();
		}
		toDispose.clear();
	}

	@Override
	public StatefulKnowledgeSession getWrappedSession() {
		checkDisposed();
		return session;
	}

	@Override
	public void registerDisposal(DroolsDisposeable disposable) {
		checkDisposed();
		toDispose.add(disposable);
	}

}
