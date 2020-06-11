package mqttxes.lib;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import mqttxes.lib.exceptions.XesMqttClientNotConnectedException;

public class XesMqttProducer extends XesMqttClient {
	
	private MqttQos qos;
	
	public XesMqttProducer(String brokerHost, String topicBase, String clientId, MqttQos qos) {
		this.topicBase = topicBase;
		this.qos = qos;
		
		client = Mqtt5Client.builder()
			.identifier(clientId)
			.serverHost(brokerHost)
			.automaticReconnect()
				.initialDelay(500, TimeUnit.MILLISECONDS)
				.maxDelay(3, TimeUnit.MINUTES)
				.applyAutomaticReconnect()
			.buildAsync();
	}
	
	public XesMqttProducer(String brokerHost, String topicBase) {
		this(brokerHost, topicBase, UUID.randomUUID().toString(), MqttQos.EXACTLY_ONCE);
	}
	
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
