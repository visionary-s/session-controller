package com.ericsson;

import com.ericsson.session.ActionType;
import com.ericsson.session.DeliverySessionCreationType;
import com.ericsson.xml.XmlBeanUtil;

import org.junit.Assert;
import org.junit.Test;

public class XmlTest {
	
	@Test
	public void BeanToXml() throws Exception {
        DeliverySessionCreationType sessionCreationType = new DeliverySessionCreationType(1L, ActionType.START, 0, 0, "test-version");
        String xml = XmlBeanUtil.BeanToXml(sessionCreationType, DeliverySessionCreationType.class);
        System.out.println(xml);
        Assert.assertNotNull(xml);
    }
	
	@Test
	public void XmlToBean() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<DeliverySession Version=\"test-version\">\n" +
                "    <DeliverySessionId>1</DeliverySessionId>\n" +
                "    <Action>Start</Action>\n" +
                "    <StartTime>0</StartTime>\n" +
                "    <StopTime>0</StopTime>\n" +
                "</DeliverySession>";
        Object sessionCreationType = XmlBeanUtil.XmlToBean(xml, DeliverySessionCreationType.class);
        Assert.assertNotNull(sessionCreationType);
        Assert.assertTrue(sessionCreationType instanceof DeliverySessionCreationType);
	}
}
