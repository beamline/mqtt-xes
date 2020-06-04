package mqttxes.lib.exceptions;

public class XesMqttClientNotConnectedException extends Exception {

	private static final long serialVersionUID = 1764128174267983057L;

	public XesMqttClientNotConnectedException() {
		super("The client is not connected");
	}
}
