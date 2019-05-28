package com.anvizent.client.scheduler.util;

import org.quartz.JobKey;
import org.quartz.Matcher;

public class GroupKeyMatcher implements Matcher<JobKey> {
	private static final long serialVersionUID = 1L;

	private String groupId;

	public GroupKeyMatcher(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public boolean isMatch(JobKey jobKey) {
		return jobKey.getGroup().equals(groupId);
	}
}
