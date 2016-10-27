package com.edlore.util;

import java.util.List;

public class BrowseUrl {

	private String deviceName;
	private List<String> listOfModelNames;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public List<String> getListOfModelNames() {
		return listOfModelNames;
	}

	public void setListOfModelNames(List<String> listOfModelNames) {
		this.listOfModelNames = listOfModelNames;
	}

	@Override
	public String toString() {
		return "BrowseUrl [deviceName=" + deviceName + ", listOfModelNames="
				+ listOfModelNames + "]";
	}

}
