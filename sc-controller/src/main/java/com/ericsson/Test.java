package com.ericsson;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.ericsson.session.ActionType;
import com.ericsson.session.DeliverySessionCreationType;

/**
 * Test.
 * @author shibiyun
 *
 */
public class Test {
	
	public static void main(String[] args) throws JAXBException, IOException {
		List<DeliverySessionCreationType> list = new ArrayList<>(100);
        for(int i = 0; i < 10; i++) {
        	list.add(new DeliverySessionCreationType((long)(Math.random() + i + 1)*10000, 
        			i % 2 == 0 ? ActionType.START : ActionType.STOP, 0, i*100, ("test-version" + i)));
        }
        
        SendSession.sendSession(list, DeliverySessionCreationType.class,"http://localhost:8080/nbi/deliverysession");
        System.out.println("=============================end====================================");
	}
}
