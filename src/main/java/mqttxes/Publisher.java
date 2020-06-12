package mqttxes;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.in.XesXmlGZIPParser;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import mqttxes.lib.XesMqttEvent;
import mqttxes.lib.XesMqttProducer;

public class Publisher {

	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
			System.out.println("Use java -jar FILE.jar log.xes.gz MILLISECONDS");
			System.exit(1);
		}
		
		int millis = Integer.parseInt(args[1]);
		
		System.out.print("Parsing log... ");
		XFactory factory = new XFactoryNaiveImpl();
		XLog log = new XesXmlGZIPParser(factory).parse(new File(args[0])).get(0);
		String logName = XConceptExtension.instance().extractName(log);
		System.out.println("Done");
		
		System.out.print("Parsing the events for streaming... ");
		List<XTrace> events = new LinkedList<XTrace>();
		for(XTrace trace : log) {
			for (XEvent event: trace) {
				XTrace t = factory.createTrace(trace.getAttributes());
				t.add(event);
				events.add(t);
			}
		}
		System.out.println("Done");
		System.out.print("Sorting the events for streaming... ");
		events.sort(new Comparator<XTrace>() {
			@Override
			public int compare(XTrace o1, XTrace o2) {
				Date d1 = XTimeExtension.instance().extractTimestamp(o1.get(0));
				Date d2 = XTimeExtension.instance().extractTimestamp(o2.get(0));
				return d1.compareTo(d2);
			}
		});
		System.out.println("Done");
		
		System.out.print("Streaming... ");
		XesMqttProducer client = new XesMqttProducer("broker.hivemq.com", "pmcep");
		client.connect();
		for (XTrace trace : events) {
			String caseId = XConceptExtension.instance().extractName(trace);
			String activity = XConceptExtension.instance().extractName(trace.get(0));
			XesMqttEvent event = new XesMqttEvent(logName, caseId, activity);
			
			event.addAllTraceAttributes(trace.getAttributes());
			event.addAllEventAttributes(trace.get(0).getAttributes());
			event.removeEventAttribute("time:timestamp");
			
			client.send(event);
			Thread.sleep(millis);
		}
		client.disconnect();
	}
}
