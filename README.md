# MQTT-XES
MQTT-XES a lightweight logging mechanism for real-time logging for process mining purposes

### Sending events
```java
XesMqttSerializer client = new XesMqttSerializer("broker.hivemq.com", "BASE");

XesMqttEvent event = new XesMqttEvent("source-id", "case-id", "activity")
    .addTraceAttribute("name", "value")
    .addEventAttribute("name", "value");
    
client.connect();
client.send(event);
client.disconnect();
```

### Consuming events
```java
XesMqttConsumer client = new XesMqttConsumer("broker.hivemq.com", "BASE");
client.subscribe(new XesMqttEventCallback() {
    @Override
    public void accept(XesMqttEvent e) {
        System.out.println(e.getProcessName() + " - " + e.getCaseId() + " - " + e.getActivityName());
    }
});
```
