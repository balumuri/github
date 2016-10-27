package com.edlore.util;

import java.util.List;

public class DetectUrl {

	private String deviceName;
	private String modelName;
	private List<String> listOfSections;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public List<String> getListOfSections() {
		return listOfSections;
	}

	public void setListOfSections(List<String> listOfSections) {
		this.listOfSections = listOfSections;
	}

	@Override
	public String toString() {
		return "DetectUrl [deviceName=" + deviceName + ", modelName="
				+ modelName + ", listOfSections=" + listOfSections + "]";
	}

}
