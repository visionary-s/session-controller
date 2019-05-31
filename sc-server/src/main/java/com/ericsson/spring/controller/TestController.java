package com.ericsson.spring.controller;

import com.ericsson.spring.session.DeliverySessionCreationType;
import com.ericsson.spring.xml.XmlBeanUtil;;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nbi")
public class TestController {
    @RequestMapping(value = "/deliverysession", method = RequestMethod.POST)
    public ResponseEntity<String> test(Long id, HttpServletRequest request)throws IOException, JAXBException{
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String xml = br.lines().collect(Collectors.joining());
        DeliverySessionCreationType session = (DeliverySessionCreationType)XmlBeanUtil.XmlToBean(xml, DeliverySessionCreationType.class);
        System.out.println("session id: " + id);
        System.out.println(session);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
