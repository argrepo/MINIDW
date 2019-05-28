package com.anvizent.minidw.service.utils.io;

import java.io.File;
import java.io.FilenameFilter;

public class FileNameContainsFilter implements FilenameFilter {

	private String match;

	public FileNameContainsFilter(String match) {
		this.match = match;
	}

	@Override
	public boolean accept(File fileDir, String name) {
		return name.matches(match);
	}

}
