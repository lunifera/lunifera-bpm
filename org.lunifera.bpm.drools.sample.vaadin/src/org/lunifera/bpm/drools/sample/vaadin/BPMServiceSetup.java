package org.lunifera.bpm.drools.sample.vaadin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.jbpm.task.Group;
import org.jbpm.task.User;
import org.jbpm.task.identity.DefaultUserGroupCallbackImpl;
import org.jbpm.task.service.DefaultUserInfo;
import org.lunifera.bpm.drools.common.server.IBPMService;
import org.lunifera.bpm.drools.common.server.commands.AddToKnowledgeBaseCommand;
import org.lunifera.bpm.drools.common.server.commands.AddUsersAndGroupsCommand;
import org.lunifera.bpm.drools.common.server.commands.SetUserGroupCallbackCommand;
import org.lunifera.bpm.drools.common.server.commands.SetUserInfoCommand;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * Is used to setup the BpmService.
 */
@Component(immediate = true)
public class BPMServiceSetup {

	private ComponentContext context;
	private IBPMService service;
	private HashMap<String, User> users;
	private HashMap<String, Group> groups;

	@Activate
	protected void activate(ComponentContext context) {
		this.context = context;

		setUsersAndGroups();
		setUserInfo();
		setSetUserGroupCallback();
		setupKnowledgeBase();

		runProcess();
	}

	@Reference(name = "bpmService", cardinality = ReferenceCardinality.MANDATORY, unbind = "unbindBpmService")
	protected synchronized void bindBpmService(IBPMService service) {
		this.service = service;
	}

	protected synchronized void unbindBpmService(IBPMService service) {
		this.service = null;
	}

	@Deactivate
	protected void deactivate(ComponentContext context) {
		this.context = null;
	}

	public void setUserInfo() {
		try {
			Properties props = new Properties();
			props.load(context.getBundleContext().getBundle()
					.getEntry("userinfo.properties").openStream());
			DefaultUserInfo userInfo = new DefaultUserInfo(props);
			SetUserInfoCommand command = new SetUserInfoCommand(userInfo);
			service.execute(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSetUserGroupCallback() {
		try {
			Properties props = new Properties();
			props.load(context.getBundleContext().getBundle()
					.getEntry("usergroups.properties").openStream());
			DefaultUserGroupCallbackImpl userInfo = new DefaultUserGroupCallbackImpl(
					props);
			SetUserGroupCallbackCommand command = new SetUserGroupCallbackCommand(
					userInfo);
			service.execute(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setUsersAndGroups() {
		users = new HashMap<String, User>();
		groups = new HashMap<String, Group>();

		users.put("Administrator", new User("Administrator"));
		users.put("florian", new User("florian"));
		users.put("klemens", new User("klemens"));
		groups.put("users", new Group("users"));
		groups.put("admin", new Group("admin"));
		groups.put("sales", new Group("sales"));

		AddUsersAndGroupsCommand addUsersCommand = new AddUsersAndGroupsCommand(
				users, groups);
		service.execute(addUsersCommand);
	}

	private void runProcess() {
		// Map<String, Object> params = new HashMap<String, Object>();
		// params.put("userId", "krisv");
		// params.put("description", "Need a new laptop computer");
		// StartProcessCommand command = new StartProcessCommand(
		// "org.lunifera.bpmn.sample", params, true);
		// service.execute(command);
	}

	/**
	 * Sets up the knowledge base.
	 */
	protected void setupKnowledgeBase() {

		// collect knowledge infos
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource(
				"files/MyFirstProcess.bpmn2", getClass().getClassLoader()),
				ResourceType.BPMN2);

		// create a setup command and execute
		AddToKnowledgeBaseCommand command = new AddToKnowledgeBaseCommand(
				kbuilder);
		service.execute(command);
	}

}
