package mqttxes;

import mqttxes.lib.XesMqttConsumer;
import mqttxes.lib.XesMqttEvent;
import mqttxes.lib.XesMqttEventCallback;

public class Consumer {

	public static void main(String[] args) throws Exception {
		
		System.out.print("Preparing client for streaming... ");
		XesMqttConsumer client = new XesMqttConsumer("broker.hivemq.com", "pmcep");
		client.subscribe(new XesMqttEventCallback() {
			@Override
			public void accept(XesMqttEvent e) {
				System.out.println(e.getProcessName() + " - " + e.getCaseId() + " - " + e.getActivityName());
			}
		});
		System.out.println("Done");
		
		client.connect();
		Thread.sleep(60 * 1000);
		client.disconnect();
	}
}
