package com.ericsson.session;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "DeliverySession")
@XmlAccessorType(value = XmlAccessType.FIELD)

public class DeliverySessionCreationType {
	
    @XmlElement(name = "DeliverySessionId")
    private long id;
    @XmlElement(name = "Action")
    @XmlJavaTypeAdapter(value = ActionTypeAdapter.class)
    private ActionType actionType;
    @XmlElement(name = "TMGIPool")
    private String tmgiPool;
    @XmlElement(name = "TMGI")
    private String tmgi;
    @XmlElement(name = "StartTime")
    private long startTime;
    @XmlElement(name = "StopTime")
    private long stopTime;
    @XmlAttribute(name = "Version")
    private String version;
    
    /**
     * 
     * @param id
     * @param actionType
     * @param startTime
     * @param stopTime
     * @param version
     */
    public DeliverySessionCreationType(long id, ActionType actionType, long startTime, long stopTime, String version) {
    	
        this.id = id;
        this.actionType = actionType;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.version = version;
        
    }
    
    public DeliverySessionCreationType() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ActionType getAction() {
        return actionType;
    }

    public void setAction(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getTmgiPool() {
    	return tmgiPool;
    }
    
    public void setTmgiPool(String tmgiPool) {
    	this.tmgiPool = tmgiPool;
    }
    
    public String getTmgi() {
    	return tmgi;
    }
    
    public void setTmgi(String tmgi) {
    	this.tmgi = tmgi;
    }
    
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }

}
