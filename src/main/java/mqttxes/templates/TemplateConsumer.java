package mqttxes.templates;

import mqttxes.lib.XesMqttConsumer;
import mqttxes.lib.XesMqttEvent;
import mqttxes.lib.XesMqttEventCallback;

public class TemplateConsumer {

	public static void main(String[] args) throws InterruptedException {

		XesMqttConsumer client = new XesMqttConsumer("broker.hivemq.com", "pmcep2");
		
		client.subscribe(new XesMqttEventCallback() {
			@Override
			public void accept(XesMqttEvent e) {
				System.out.println(e.getProcessName() + " - " + e.getCaseId() + " - " + e.getActivityName());
			}
		});
		
		client.connect();
		
		Thread.sleep(1000 * 60);
		
		client.disconnect();
	}
}
