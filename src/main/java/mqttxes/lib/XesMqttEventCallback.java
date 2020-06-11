package mqttxes.lib;

import java.util.List;
import java.util.function.Consumer;

import org.json.simple.parser.ParseException;

import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

public abstract class XesMqttEventCallback implements Consumer<Mqtt5Publish> {

	
	public abstract void accept(XesMqttEvent e);
	
	@Override
	public void accept(Mqtt5Publish t) {
		List<String> l = t.getTopic().getLevels();
		String activityName = l.get(l.size() - 1);
		String caseId = l.get(l.size() - 2);
		String processName = l.get(l.size() - 3);
		
		XesMqttEvent e = new XesMqttEvent(processName, caseId, activityName);
		try {
			e.setAttributes(new String(t.getPayloadAsBytes()));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		accept(e);
	}
}
