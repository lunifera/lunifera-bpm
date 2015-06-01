package org.lunifera.bpm.drools.ui.vaaclipse.context;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.jbpm.eventmessaging.EventKey;
import org.jbpm.eventmessaging.EventResponseHandler;
import org.jbpm.task.Attachment;
import org.jbpm.task.Comment;
import org.jbpm.task.Content;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.service.FaultData;

/**
 * A disposable version of task service.
 */
public class DisposeableTaskService implements TaskService {

	@Inject
	@Named("delegatingTaskService")
	private TaskService delegate;

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#activate(long, java.lang.String)
	 */
	public void activate(long taskId, String userId) {
		delegate.activate(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param attachment
	 * @param content
	 * @see org.jbpm.task.TaskService#addAttachment(long,
	 *      org.jbpm.task.Attachment, org.jbpm.task.Content)
	 */
	public void addAttachment(long taskId, Attachment attachment,
			Content content) {
		delegate.addAttachment(taskId, attachment, content);
	}

	/**
	 * @param taskId
	 * @param comment
	 * @see org.jbpm.task.TaskService#addComment(long, org.jbpm.task.Comment)
	 */
	public void addComment(long taskId, Comment comment) {
		delegate.addComment(taskId, comment);
	}

	/**
	 * @param task
	 * @param content
	 * @see org.jbpm.task.TaskService#addTask(org.jbpm.task.Task,
	 *      org.jbpm.task.service.ContentData)
	 */
	public void addTask(Task task, ContentData content) {
		delegate.addTask(task, content);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#claim(long, java.lang.String)
	 */
	public void claim(long taskId, String userId) {
		delegate.claim(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param groupIds
	 * @deprecated
	 * @see org.jbpm.task.TaskService#claim(long, java.lang.String,
	 *      java.util.List)
	 */
	public void claim(long taskId, String userId, List<String> groupIds) {
		delegate.claim(taskId, userId, groupIds);
	}

	/**
	 * @param userId
	 * @param language
	 * @see org.jbpm.task.TaskService#claimNextAvailable(java.lang.String,
	 *      java.lang.String)
	 */
	public void claimNextAvailable(String userId, String language) {
		delegate.claimNextAvailable(userId, language);
	}

	/**
	 * @param userId
	 * @param groupIds
	 * @param language
	 * @deprecated
	 * @see org.jbpm.task.TaskService#claimNextAvailable(java.lang.String,
	 *      java.util.List, java.lang.String)
	 */
	public void claimNextAvailable(String userId, List<String> groupIds,
			String language) {
		delegate.claimNextAvailable(userId, groupIds, language);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param outputData
	 * @see org.jbpm.task.TaskService#complete(long, java.lang.String,
	 *      org.jbpm.task.service.ContentData)
	 */
	public void complete(long taskId, String userId, ContentData outputData) {
		delegate.complete(taskId, userId, outputData);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param results
	 * @see org.jbpm.task.TaskService#completeWithResults(long,
	 *      java.lang.String, java.lang.Object)
	 */
	public void completeWithResults(long taskId, String userId, Object results) {
		delegate.completeWithResults(taskId, userId, results);
	}

	/**
	 * @return
	 * @see org.jbpm.task.TaskService#connect()
	 */
	public boolean connect() {
		return delegate.connect();
	}

	/**
	 * @param address
	 * @param port
	 * @return
	 * @see org.jbpm.task.TaskService#connect(java.lang.String, int)
	 */
	public boolean connect(String address, int port) {
		return delegate.connect(address, port);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param targetUserId
	 * @see org.jbpm.task.TaskService#delegate(long, java.lang.String,
	 *      java.lang.String)
	 */
	public void delegate(long taskId, String userId, String targetUserId) {
		delegate.delegate(taskId, userId, targetUserId);
	}

	/**
	 * @param taskId
	 * @param attachmentId
	 * @param contentId
	 * @see org.jbpm.task.TaskService#deleteAttachment(long, long, long)
	 */
	public void deleteAttachment(long taskId, long attachmentId, long contentId) {
		delegate.deleteAttachment(taskId, attachmentId, contentId);
	}

	/**
	 * @param taskId
	 * @param commentId
	 * @see org.jbpm.task.TaskService#deleteComment(long, long)
	 */
	public void deleteComment(long taskId, long commentId) {
		delegate.deleteComment(taskId, commentId);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#deleteFault(long, java.lang.String)
	 */
	public void deleteFault(long taskId, String userId) {
		delegate.deleteFault(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#deleteOutput(long, java.lang.String)
	 */
	public void deleteOutput(long taskId, String userId) {
		delegate.deleteOutput(taskId, userId);
	}

	/**
	 * @throws Exception
	 * @see org.jbpm.task.TaskService#disconnect()
	 */
	public void disconnect() throws Exception {
		delegate.disconnect();
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#exit(long, java.lang.String)
	 */
	public void exit(long taskId, String userId) {
		delegate.exit(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param faultData
	 * @see org.jbpm.task.TaskService#fail(long, java.lang.String,
	 *      org.jbpm.task.service.FaultData)
	 */
	public void fail(long taskId, String userId, FaultData faultData) {
		delegate.fail(taskId, userId, faultData);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param targetEntityId
	 * @see org.jbpm.task.TaskService#forward(long, java.lang.String,
	 *      java.lang.String)
	 */
	public void forward(long taskId, String userId, String targetEntityId) {
		delegate.forward(taskId, userId, targetEntityId);
	}

	/**
	 * @param contentId
	 * @return
	 * @see org.jbpm.task.TaskService#getContent(long)
	 */
	public Content getContent(long contentId) {
		return delegate.getContent(contentId);
	}

	/**
	 * @param parentId
	 * @param userId
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getSubTasksAssignedAsPotentialOwner(long,
	 *      java.lang.String, java.lang.String)
	 */
	public List<TaskSummary> getSubTasksAssignedAsPotentialOwner(long parentId,
			String userId, String language) {
		return delegate.getSubTasksAssignedAsPotentialOwner(parentId, userId,
				language);
	}

	/**
	 * @param parentId
	 * @return
	 * @see org.jbpm.task.TaskService#getSubTasksByParent(long)
	 */
	public List<TaskSummary> getSubTasksByParent(long parentId) {
		return delegate.getSubTasksByParent(parentId);
	}

	/**
	 * @param taskId
	 * @return
	 * @see org.jbpm.task.TaskService#getTask(long)
	 */
	public Task getTask(long taskId) {
		return delegate.getTask(taskId);
	}

	/**
	 * @param workItemId
	 * @return
	 * @see org.jbpm.task.TaskService#getTaskByWorkItemId(long)
	 */
	public Task getTaskByWorkItemId(long workItemId) {
		return delegate.getTaskByWorkItemId(workItemId);
	}

	/**
	 * @param userId
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsBusinessAdministrator(java.lang.String,
	 *      java.lang.String)
	 */
	public List<TaskSummary> getTasksAssignedAsBusinessAdministrator(
			String userId, String language) {
		return delegate.getTasksAssignedAsBusinessAdministrator(userId,
				language);
	}

	/**
	 * @param userId
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsExcludedOwner(java.lang.String,
	 *      java.lang.String)
	 */
	public List<TaskSummary> getTasksAssignedAsExcludedOwner(String userId,
			String language) {
		return delegate.getTasksAssignedAsExcludedOwner(userId, language);
	}

	/**
	 * @param userId
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsPotentialOwner(java.lang.String,
	 *      java.lang.String)
	 */
	public List<TaskSummary> getTasksAssignedAsPotentialOwner(String userId,
			String language) {
		return delegate.getTasksAssignedAsPotentialOwner(userId, language);
	}

	/**
	 * @param userId
	 * @param groupIds
	 * @param language
	 * @return
	 * @deprecated
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsPotentialOwner(java.lang.String,
	 *      java.util.List, java.lang.String)
	 */
	public List<TaskSummary> getTasksAssignedAsPotentialOwner(String userId,
			List<String> groupIds, String language) {
		return delegate.getTasksAssignedAsPotentialOwner(userId, groupIds,
				language);
	}

	/**
	 * @param userId
	 * @param groupIds
	 * @param language
	 * @param firstResult
	 * @param maxResult
	 * @return
	 * @deprecated
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsPotentialOwner(java.lang.String,
	 *      java.util.List, java.lang.String, int, int)
	 */
	public List<TaskSummary> getTasksAssignedAsPotentialOwner(String userId,
			List<String> groupIds, String language, int firstResult,
			int maxResult) {
		return delegate.getTasksAssignedAsPotentialOwner(userId, groupIds,
				language, firstResult, maxResult);
	}

	/**
	 * @param userId
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsRecipient(java.lang.String,
	 *      java.lang.String)
	 */
	public List<TaskSummary> getTasksAssignedAsRecipient(String userId,
			String language) {
		return delegate.getTasksAssignedAsRecipient(userId, language);
	}

	/**
	 * @param userId
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsTaskInitiator(java.lang.String,
	 *      java.lang.String)
	 */
	public List<TaskSummary> getTasksAssignedAsTaskInitiator(String userId,
			String language) {
		return delegate.getTasksAssignedAsTaskInitiator(userId, language);
	}

	/**
	 * @param userId
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsTaskStakeholder(java.lang.String,
	 *      java.lang.String)
	 */
	public List<TaskSummary> getTasksAssignedAsTaskStakeholder(String userId,
			String language) {
		return delegate.getTasksAssignedAsTaskStakeholder(userId, language);
	}

	/**
	 * @param userId
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksOwned(java.lang.String,
	 *      java.lang.String)
	 */
	public List<TaskSummary> getTasksOwned(String userId, String language) {
		return delegate.getTasksOwned(userId, language);
	}

	/**
	 * @param userId
	 * @param status
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksOwned(java.lang.String,
	 *      java.util.List, java.lang.String)
	 */
	public List<TaskSummary> getTasksOwned(String userId, List<Status> status,
			String language) {
		return delegate.getTasksOwned(userId, status, language);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param potentialOwners
	 * @see org.jbpm.task.TaskService#nominate(long, java.lang.String,
	 *      java.util.List)
	 */
	public void nominate(long taskId, String userId,
			List<OrganizationalEntity> potentialOwners) {
		delegate.nominate(taskId, userId, potentialOwners);
	}

	/**
	 * @param qlString
	 * @param size
	 * @param offset
	 * @return
	 * @deprecated
	 * @see org.jbpm.task.TaskService#query(java.lang.String, java.lang.Integer,
	 *      java.lang.Integer)
	 */
	public List<?> query(String qlString, Integer size, Integer offset) {
		return delegate.query(qlString, size, offset);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#register(long, java.lang.String)
	 */
	public void register(long taskId, String userId) {
		delegate.register(taskId, userId);
	}

	/**
	 * @param key
	 * @param remove
	 * @param responseHandler
	 * @see org.jbpm.task.TaskService#registerForEvent(org.jbpm.eventmessaging.EventKey,
	 *      boolean, org.jbpm.eventmessaging.EventResponseHandler)
	 */
	public void registerForEvent(EventKey key, boolean remove,
			EventResponseHandler responseHandler) {
		delegate.registerForEvent(key, remove, responseHandler);
	}

	/**
	 * @param key
	 * @see org.jbpm.task.TaskService#unregisterForEvent(org.jbpm.eventmessaging.EventKey)
	 */
	public void unregisterForEvent(EventKey key) {
		delegate.unregisterForEvent(key);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#release(long, java.lang.String)
	 */
	public void release(long taskId, String userId) {
		delegate.release(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#remove(long, java.lang.String)
	 */
	public void remove(long taskId, String userId) {
		delegate.remove(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#resume(long, java.lang.String)
	 */
	public void resume(long taskId, String userId) {
		delegate.resume(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param content
	 * @see org.jbpm.task.TaskService#setDocumentContent(long,
	 *      org.jbpm.task.Content)
	 */
	public void setDocumentContent(long taskId, Content content) {
		delegate.setDocumentContent(taskId, content);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param fault
	 * @see org.jbpm.task.TaskService#setFault(long, java.lang.String,
	 *      org.jbpm.task.service.FaultData)
	 */
	public void setFault(long taskId, String userId, FaultData fault) {
		delegate.setFault(taskId, userId, fault);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param outputContentData
	 * @see org.jbpm.task.TaskService#setOutput(long, java.lang.String,
	 *      org.jbpm.task.service.ContentData)
	 */
	public void setOutput(long taskId, String userId,
			ContentData outputContentData) {
		delegate.setOutput(taskId, userId, outputContentData);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @param priority
	 * @see org.jbpm.task.TaskService#setPriority(long, java.lang.String, int)
	 */
	public void setPriority(long taskId, String userId, int priority) {
		delegate.setPriority(taskId, userId, priority);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#skip(long, java.lang.String)
	 */
	public void skip(long taskId, String userId) {
		delegate.skip(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#start(long, java.lang.String)
	 */
	public void start(long taskId, String userId) {
		delegate.start(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#stop(long, java.lang.String)
	 */
	public void stop(long taskId, String userId) {
		delegate.stop(taskId, userId);
	}

	/**
	 * @param taskId
	 * @param userId
	 * @see org.jbpm.task.TaskService#suspend(long, java.lang.String)
	 */
	public void suspend(long taskId, String userId) {
		delegate.suspend(taskId, userId);
	}

	/**
	 * @param userId
	 * @param status
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsPotentialOwnerByStatus(java.lang.String,
	 *      java.util.List, java.lang.String)
	 */
	public List<TaskSummary> getTasksAssignedAsPotentialOwnerByStatus(
			String userId, List<Status> status, String language) {
		return delegate.getTasksAssignedAsPotentialOwnerByStatus(userId,
				status, language);
	}

	/**
	 * @param userId
	 * @param groupIds
	 * @param status
	 * @param language
	 * @return
	 * @deprecated
	 * @see org.jbpm.task.TaskService#getTasksAssignedAsPotentialOwnerByStatusByGroup(java.lang.String,
	 *      java.util.List, java.util.List, java.lang.String)
	 */
	public List<TaskSummary> getTasksAssignedAsPotentialOwnerByStatusByGroup(
			String userId, List<String> groupIds, List<Status> status,
			String language) {
		return delegate.getTasksAssignedAsPotentialOwnerByStatusByGroup(userId,
				groupIds, status, language);
	}

	/**
	 * @param processInstanceId
	 * @param status
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksByStatusByProcessId(long,
	 *      java.util.List, java.lang.String)
	 */
	public List<TaskSummary> getTasksByStatusByProcessId(
			long processInstanceId, List<Status> status, String language) {
		return delegate.getTasksByStatusByProcessId(processInstanceId, status,
				language);
	}

	/**
	 * @param processInstanceId
	 * @param status
	 * @param taskName
	 * @param language
	 * @return
	 * @see org.jbpm.task.TaskService#getTasksByStatusByProcessIdByTaskName(long,
	 *      java.util.List, java.lang.String, java.lang.String)
	 */
	public List<TaskSummary> getTasksByStatusByProcessIdByTaskName(
			long processInstanceId, List<Status> status, String taskName,
			String language) {
		return delegate.getTasksByStatusByProcessIdByTaskName(
				processInstanceId, status, taskName, language);
	}

	@PreDestroy
	protected void dispose() {
		try {
			delegate.disconnect();
		} catch (Exception e) {
		}
		delegate = null;
	}

}
