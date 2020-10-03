package mqttxes.templates;

import java.util.Random;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XEventImpl;
import org.deckfour.xes.model.impl.XTraceImpl;

import mqttxes.lib.XesMqttEvent;
import mqttxes.lib.XesMqttProducer;
import mqttxes.lib.exceptions.XesMqttClientNotConnectedException;


public class TemplateProducer {

	public static void main(String[] args) throws XesMqttClientNotConnectedException {
		
		XesMqttProducer client = new XesMqttProducer("broker.hivemq.com", "xes");
		client.connect();
		
		int i = 0;
		while (true) {
			XesMqttEvent event = new XesMqttEvent(
					ProcessName(),
					"Case-id-" + i,
					Activity());
			
			XTrace t = getTrace();
			event.addAllTraceAttributes(t.getAttributes());
			
			XEvent e = getEvent();
			event.addAllEventAttributes(e.getAttributes());
			
			event.addEventAttribute("testEvent", "test");
			event.addTraceAttribute("testTrace", "test");
			client.send(event);
			
			i++;
		}
	}
	
	public static String Activity() {
		return "Act " + (char)(new Random().nextInt(26) + 'a');
	}
	public static String ProcessName() {
		return "P" + new Random().nextInt(5);
	}
	public static XTrace getTrace() {
		XTrace t = new XTraceImpl(new XAttributeMapImpl());
		t.getAttributes().put("trace-attribute", new XAttributeLiteralImpl("trace-attribute", "trace-value"));
		return t;
	}
	public static XEvent getEvent() {
		XEvent e = new XEventImpl(new XAttributeMapImpl());
		e.getAttributes().put("event-attribute", new XAttributeLiteralImpl("event-attribute", "event-value"));
		return e;
	}
}
