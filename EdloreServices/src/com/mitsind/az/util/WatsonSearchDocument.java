package com.mitsind.az.util;

public class WatsonSearchDocument {

	private String name;
	private String mimetype;
	private String title;
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "WatsonSearchDocument [name=" + name + ", mimetype=" + mimetype
				+ ", title=" + title + ", url=" + url + "]";
	}

}