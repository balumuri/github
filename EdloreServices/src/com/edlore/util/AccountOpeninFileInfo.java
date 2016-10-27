package com.edlore.util;

public class AccountOpeninFileInfo {

	private String fileName;
	private String url;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "AccountOpeninFileInfo [fileName=" + fileName + ", url=" + url
				+ "]";
	}

}