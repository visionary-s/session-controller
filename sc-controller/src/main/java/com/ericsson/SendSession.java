package com.ericsson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.httpClient.HttpClientUtil;
import com.ericsson.session.ActionType;
import com.ericsson.session.DeliverySessionCreationType;
import com.ericsson.threadPool.ThreadPool;
import com.ericsson.xml.XmlBeanUtil;

/**
 * Util class for sending session.
 * @author shibiyun
 *
 */
public class SendSession {
	
	private static final Logger logger = LoggerFactory.getLogger(SendSession.class);
	// Common session thread pool
	private static final ThreadPoolExecutor sessionPool = ThreadPool.instance.getThreadPool();
	// Timed session thread pool
	private static final ScheduledExecutorService timedPool = ThreadPool.instance.getScheduledThreadPool();
	
	private SendSession(){
		
	}
	
	/**
	 * Convert session bean to xml string and send over in bulk.
	 * @param sessions
	 * @param clazz
	 * @param url
	 */
	public static void sendSession(List<DeliverySessionCreationType> sessions, Class<?> clazz, String url) {
		
		sessions.stream().forEach(session -> {
            sessionPool.execute(() -> {
                try {
                    divideSession(session, clazz, url);
                } catch (JAXBException e) {
                    logger.error("date: " + new Date());
                    logger.error("parse bean to xml error.");
                    logger.error(e.toString() + " : " + e.getCause().getMessage());
                } catch (IOException e) {
                    logger.error("date: " + new Date());
                    logger.error("send data error.");
                    logger.error(e.toString() + " : " + e.getCause().getMessage());
                }
            });
        });	
	}
	
	/**
	 * Divide session according to ActionType.
	 * @param session
	 * @param clazz
	 * @param url
	 * @throws JAXBException
	 * @throws IOException
	 */
	private static void divideSession(DeliverySessionCreationType session, Class<?> clazz, String url) throws JAXBException, IOException {
        
		switch (session.getAction()) {
            case START:
                sendSession(session, clazz, url);
                // After sent, set ActionType to STOP
                session.setAction(ActionType.STOP);
                // Add session to scheduled pool
                doSchedule(session, clazz, url);
                break;
            case STOP:
                doSchedule(session, clazz, url);
                break;
            default:
                logger.error("date: " + new Date());
                logger.error("action type is illegal");
                break;
        }
    }
	
	/**
	 * Send scheduled session with ActionType "STOP".
	 * @param session
	 * @param clazz
	 * @param url
	 */
	private static void doSchedule(DeliverySessionCreationType session, Class<?> clazz, String url) {
        
		timedPool.schedule(() -> {
            try {
                sendSession(session, clazz, url);
            } catch (JAXBException e) {
                logger.error("date: " + new Date());
                logger.error("parse bean to xml error.");
                logger.error(e.toString() + " : " + e.getCause().getMessage());
            } catch (IOException e) {
                logger.error("date: " + new Date());
                logger.error("send data error.");
                logger.error(e.toString() + " : " + e.getCause().getMessage());
            }
        },(session.getStopTime() - session.getStartTime()), TimeUnit.MILLISECONDS);
    }
	
	/**
	 * Convert session bean to XML String and send it to server.
	 * @param session
	 * @param clazz
	 * @param url
	 * @throws JAXBException
	 * @throws IOException
	 */
	private static void sendSession(DeliverySessionCreationType session, Class<?> clazz, String url) throws JAXBException, IOException{
        
		String data = XmlBeanUtil.BeanToXml(session, clazz);
        url = new StringBuffer(url).append("?id=").append(session.getId()).toString();
        httpClientSend(data, url, "text/xml;charset=utf-8");
        
    }
		
	/**
	 * Send data via HttpClient.
	 * @param data
	 * @param url
	 * @param contentType
	 * @throws IOException
	 */
	private static synchronized void httpClientSend(String data, String url, String contentType) throws IOException {
		
        HttpClient client = HttpClientUtil.getHttpClient();
        
        HttpPost post = new HttpPost(url);	// Setup post request
        
        post.setHeader("content-type",contentType);	// Set type of content
        
        StringEntity entity = new StringEntity(data, Charset.forName("UTF-8"));	// Add data to the posting entity
        post.setEntity(entity);

        Map<String, String> map = new HashMap<>(5);
        				
        map.put("send date",new Date().toString());
        map.put("url", url);
        map.put("body",data);
        recordLog(map);
        map.clear();

        HttpResponse response = client.execute(post);
        
        map.put("receive date",new Date().toString());
        map.put("code", String.valueOf(response.getStatusLine().getStatusCode()));
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String responseEntity = br.lines().collect(Collectors.joining());
        map.put("responseEntity",responseEntity);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            map.put("result","success");
        } 
        else {
            map.put("result","fail");
        }
        recordLog(map);
        map.clear();
        map.put("===","==============================================================================");
        recordLog(map);
    }
	
	/**
	 * Record log.
	 * @param map
	 */
	private static void recordLog(Map<String, String> map){
        
		map.entrySet().stream()
        .forEach( entry -> logger.info(entry.getKey() + ":" + entry.getValue()));
    
	}

}
