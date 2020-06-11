package mqttxes.lib;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;

public abstract class XesMqttClient {

	protected String topicBase;
	protected boolean clientIsConnected = false;
	protected Mqtt5AsyncClient client;

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
}
