package mqttxes.lib;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

public class XesMqttConsumer extends XesMqttClient {
	
	public XesMqttConsumer(String brokerHost, String topicBase, String clientId) {
		this.topicBase = topicBase;
		
		client = Mqtt5Client.builder()
				.identifier(clientId)
				.serverHost(brokerHost)
				.automaticReconnect()
					.initialDelay(500, TimeUnit.MILLISECONDS)
					.maxDelay(3, TimeUnit.MINUTES)
					.applyAutomaticReconnect()
				.buildAsync();
	}
	
	public XesMqttConsumer(String brokerHost, String topicBase) {
		this(brokerHost, topicBase, UUID.randomUUID().toString());
	}
	
	public void subscribe(XesMqttEventCallback callback) {
		subscribe(null, callback);
	}
	
	public void subscribe(String processName, XesMqttEventCallback callback) {
		String topic = topicBase + "/" + processName + "/#";
		if (processName == null) {
			topic = topicBase + "/#";
		}
		
		client.subscribeWith()
			.topicFilter(topic)
			.callback(callback)
			.send();
		}
}
