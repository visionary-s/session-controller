 package com.ericsson.session;

 /**
  * Define action type.
  * @author shibiyun
  *
  */
public enum ActionType {
	START("Start"), STOP("Stop");
	
	private String value;
	
	ActionType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
