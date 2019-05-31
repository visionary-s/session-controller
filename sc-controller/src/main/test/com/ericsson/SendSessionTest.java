package com.ericsson;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.SendSession;
import com.ericsson.session.ActionType;
import com.ericsson.session.DeliverySessionCreationType;

/**
 * Unit test for sending sessions.
 */
public class SendSessionTest {
    
	private List<DeliverySessionCreationType> sessions = null;
    private static final int SIZE = 10;
    private String url = null;
    private static final Logger logger = LoggerFactory.getLogger(SendSessionTest.class);
	
    @Before
    public void init() {
    	
    	url = "http://localhost:8080/nbi/deliverysession";
    	
    	sessions = new ArrayList<>(SIZE);
        for(int i = 0; i < SIZE; i++){
            sessions.add(new DeliverySessionCreationType((long)Math.random() * 1000000, 
            		(i % 2 == 0 ? ActionType.START : ActionType.STOP), 
            		0, (long)Math.random() * 10000, ("test-version" + i)));
        }
    }
    
    @Test
    public void sendSessionTest() throws Exception {
    	SendSession.sendSession(sessions, DeliverySessionCreationType.class, url);
    }
}
