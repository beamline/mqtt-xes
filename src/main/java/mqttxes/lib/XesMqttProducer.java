package mqttxes.lib;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import mqttxes.lib.exceptions.XesMqttClientNotConnectedException;

/**
 * Producer client, useful to send new events
 * 
 * @author Andrea Burattin
 */
public class XesMqttProducer extends XesMqttClient {
	
	private MqttQos qos;
	
	/**
	 * 
	 * @param brokerHost
	 * @param topicBase
	 * @param clientId
	 * @param qos
	 */
	public XesMqttProducer(String brokerHost, String topicBase, String clientId, MqttQos qos) {
		this.topicBase = topicBase;
		this.qos = qos;
		
		client = Mqtt5Client.builder()
			.identifier(clientId)
			.serverHost(brokerHost)
			.serverPort(8883)
			.sslWithDefaultConfig()
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
	public XesMqttProducer(String brokerHost, String topicBase) {
		this(brokerHost, topicBase, UUID.randomUUID().toString(), MqttQos.EXACTLY_ONCE);
	}
	
	/**
	 * 
	 * @param event
	 * @throws XesMqttClientNotConnectedException
	 */
	public void send(XesMqttEvent event) throws XesMqttClientNotConnectedException {
		if (!clientIsConnected) {
			throw new XesMqttClientNotConnectedException();
		}
		
		client.publishWith().topic(
				topicBase + "/" +
				event.getProcessName() + "/" +
				event.getCaseId() + "/" + 
				event.getActivityName())
			.qos(qos)
			.payload(event.getAttributes().getBytes())
			.send();
	}
}
