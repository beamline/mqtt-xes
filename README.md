# MQTT-XES

MQTT-XES a lightweight logging mechanism for real-time logging for process mining purposes

### Installing the library

To install the library using Maven, first you need to include, in your `pom.xml` file, the package repository:
```xml
<repositories>
	<repository>
		<id>beamline</id>
		<name></name>
		<url>https://dl.bintray.com/delas/beamline</url>
	</repository>
</repositories>
```
Then you can include the dependency to the version you are interested, for example:
```xml
<dependency>
	<groupId>beamline</groupId>
	<artifactId>mqtt-xes</artifactId>
	<version>0.3.2</version>
</dependency>
```


### Sending events

To generate events to be sent using MQTT-XES it is possible ot use the following code snippet, first to create the client:
```java
XesMqttSerializer client = new XesMqttSerializer("broker.hivemq.com", "BASE");
```
It is also necessary to create the event that has to be sent:
```java
XesMqttEvent event = new XesMqttEvent("source-id", "case-id", "activity")
    .addTraceAttribute("name", "value")
    .addEventAttribute("name", "value");
```
Finally, it is possible to send the event using the client object previously defined:
```java
client.connect();
client.send(event);
client.disconnect();
```

### Consuming events

To consume events, it is first necessary to create a consumer client, using the following code snippet:
```java
XesMqttConsumer client = new XesMqttConsumer("broker.hivemq.com", "BASE");
```
Once the client is set, it is possible to subscribe to the MQTT-XES events being sent and a callback class need to be provided. Please note that the `accept` method of `XesMqttEventCallback` receives a XesMqttEvent:
```java
client.subscribe(new XesMqttEventCallback() {
    @Override
    public void accept(XesMqttEvent e) {
        System.out.println(e.getProcessName() + " - " + e.getCaseId() + " - " + e.getActivityName());
    }
});
```
