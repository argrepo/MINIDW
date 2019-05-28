package com.datamodel.anvizent.service.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class DataResponse {

	private List<Message> messages = new ArrayList<>();
	private Object obj;

	public List<Message> getMessages() {
		return messages;
	}

	public void addMessage(Message message) {
		if (messages == null) {
			messages = new ArrayList<>();
		}

		messages.add(message);
	}

	public void addMessages(Collection<Message> messages) {
		if (this.messages == null) {
			this.messages = new ArrayList<>();
		}

		this.messages.addAll(messages);
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Object getObject() {
		return obj;
	}

	public void setObject(Object object) {
		this.obj = object;
	}

	public Boolean getHasMessages() {
		Boolean hasMessages = Boolean.FALSE;
		if (messages != null) {
			for (Message message : messages) {
				if (message.getCode() != null) {
					hasMessages = Boolean.TRUE;
					break;
				}
			}
		}
		return hasMessages;
	}

	@Override
	public String toString() {
		return "DataResponse [messages=" + messages + ", obj=" + obj + "]";
	}

}
