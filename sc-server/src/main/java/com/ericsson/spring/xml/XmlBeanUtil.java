package com.ericsson.spring.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Util class for conversion between JavaBean and XML.
 * @author shibiyun
 *
 */
public class XmlBeanUtil {
	
	/**
	 * Parse JavaBean to XML.
	 * @param object
	 * @param className
	 * @return
	 */
	public static String BeanToXml(Object object, Class<?> className) {
		
		StringWriter sw = null;
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(className);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			sw = new StringWriter();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");
			jaxbMarshaller.marshal(object, sw);
		} catch(JAXBException e) {
			e.printStackTrace();
		}
		
		return sw.toString();
	}
	
	/**
	 * Parse XML to JavaBean.
	 * @param xml
	 * @param className
	 * @return
	 */
	public static Object XmlToBean(String xml, Class<?> className) {
		
		StringReader sr = null;
		Object object = null;
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(className);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			sr = new StringReader(xml);
			object = jaxbUnmarshaller.unmarshal(sr);
		} catch(JAXBException e) {
			e.printStackTrace();
		}
		
		return object;
	}
}
