package com.edlore.util;

public class Asset {

	private String upload_file_name;
	private String download_url;

	
	public String getUpload_file_name() {
		return upload_file_name;
	}

	public void setUpload_file_name(String upload_file_name) {
		this.upload_file_name = upload_file_name;
	}

	public String getDownload_url() {
		return download_url;
	}

	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

	@Override
	public String toString() {
		return "Asset [upload_file_name=" + upload_file_name
				+ ", download_url=" + download_url + "]";
	}
	
}
