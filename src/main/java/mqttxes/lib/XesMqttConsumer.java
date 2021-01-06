package mqttxes.lib;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

/**
 * Consumer client, useful to set callback when events are received
 * 
 * @author Andrea Burattin
 */
public class XesMqttConsumer extends XesMqttClient {
	
	/**
	 * 
	 * @param brokerHost
	 * @param topicBase
	 * @param clientId
	 */
	public XesMqttConsumer(String brokerHost, String topicBase, String clientId) {
		this.topicBase = topicBase;
		
		client = Mqtt5Client.builder()
				.identifier(clientId)
				.serverHost(brokerHost)
				.serverPort(8883)
				.automaticReconnect()
					.initialDelay(500, TimeUnit.MILLISECONDS)
					.maxDelay(3, TimeUnit.MINUTES)
					.applyAutomaticReconnect()
				.buildAsync();
	}
	
	/**
	 * This constructor generates a new client using a random value for the
	 * client id
	 * 
	 * @param brokerHost
	 * @param topicBase
	 */
	public XesMqttConsumer(String brokerHost, String topicBase) {
		this(brokerHost, topicBase, UUID.randomUUID().toString());
	}
	
	/**
	 * Method to subscribe to events from all processes
	 * 
	 * @param callback
	 */
	public void subscribe(XesMqttEventCallback callback) {
		subscribe(null, callback);
	}
	
	/**
	 * Method to subscribe to events from just to one specific process
	 * 
	 * @param processName
	 * @param callback
	 */
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
