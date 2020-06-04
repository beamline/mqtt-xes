import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.in.XParser;
import org.deckfour.xes.in.XesXmlGZIPParser;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.out.XSerializer;
import org.deckfour.xes.out.XesXmlSerializer;

import com.google.common.collect.Lists;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

public class Publisher {

	public static void main(String[] args) throws Exception {
		
		if (args.length == 0) {
			System.out.println("Use java -jar FILE.jar log.xes.gz");
			System.exit(1);
		}
		
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
		
		Mqtt5BlockingClient client = Mqtt5Client.builder()
				.identifier(UUID.randomUUID().toString())
				.serverHost("broker.hivemq.com")
				.buildBlocking();
		client.connect();
		
		System.out.print("Streaming... ");
		XSerializer serializer = new XesXmlSerializer();
		for (XTrace trace : events) {
			String caseId = XConceptExtension.instance().extractName(trace);
			String activity = XConceptExtension.instance().extractName(trace.get(0));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			XLog l = factory.createLog();
			l.add(trace);
			serializer.serialize(l, out);
			
			client.publishWith().topic("pmcep/" + logName + "/" + caseId + "/" + activity)
				.qos(MqttQos.AT_LEAST_ONCE)
				.payload(out.toByteArray())
				.send();
			Thread.sleep(100);
		}
		client.disconnect();
		System.out.println("Done");
	}
}
