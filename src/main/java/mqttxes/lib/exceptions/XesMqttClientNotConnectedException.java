package mqttxes.lib.exceptions;

/**
 * Exception thrown when there are connection problems
 * 
 * @author Andrea Burattin
 */
public class XesMqttClientNotConnectedException extends Exception {

	private static final long serialVersionUID = 1764128174267983057L;

	public XesMqttClientNotConnectedException() {
		super("The client is not connected");
	}
}
