Broadcast Session Controller
===========================
Statement
---
Control the start and stop of sessions.<br>


Core ideas
---
1. The client control the session life cycle by sending the session start/stop request to the server.<br>
2. Session stored as java bean DeliverySessionCreationType.<br>
3. Use JAXB to build util class XmlBeanUtil, convert session info into String data inside http-body via BeanToXml, then excute post request by httpClient and send data to server.<br>
4. Use SessionThreadPool to construct thread pool， and send session start data by asynchronous concurrent thread. Setup executable ScheduledExecutorService and set delay to task with session stop according to given interval between start and stop state.<br>
5. Setup connection pool to reduce system overhead.
6. For UT, cover functions under two client class XmlBeanUtil and SendSession.<br>

UML graph
---
![类图](https://github.com/visionary-s/session-controller/raw/master/graph/class-graph.png)
