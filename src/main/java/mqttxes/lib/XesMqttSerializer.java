package mqttxes.lib;

import java.util.UUID;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import mqttxes.lib.exceptions.XesMqttClientNotConnectedException;

public class XesMqttSerializer {
	
	private String topicBase;
	private boolean clientIsConnected = false;
	private Mqtt5BlockingClient client;
	private MqttQos qos;
	
	public XesMqttSerializer(String brokerHost, String topicBase, String clientId, MqttQos qos) {
		this.topicBase = topicBase;
		this.qos = qos;
		
		client = Mqtt5Client.builder().identifier(clientId).serverHost(brokerHost).buildBlocking();
	}
	
	public XesMqttSerializer(String brokerHost, String topicBase) {
		this(brokerHost, topicBase, UUID.randomUUID().toString(), MqttQos.EXACTLY_ONCE);
	}
	
	public void connect() {
		if (!clientIsConnected) {
			client.connect();
			clientIsConnected = true;
		}
	}
	
	public void disconnect() {
		if (clientIsConnected) {
			client.disconnect();
		} 
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
