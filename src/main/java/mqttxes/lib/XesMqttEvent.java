package mqttxes.lib;

import java.util.HashMap;
import java.util.Map;

import org.deckfour.xes.model.XAttributeMap;
import org.json.simple.JSONObject;

public class XesMqttEvent {

	private String processName;
	private String caseId;
	private String activityName;
	private Map<String, HashMap<String, String>> attributes = new HashMap<String, HashMap<String, String>>();
	
	public XesMqttEvent(String processName, String caseId, String activityName) {
		this.processName = processName;
		this.caseId = caseId;
		this.activityName = activityName;
	}
	
	public String getProcessName() {
		return processName;
	}

	public String getCaseId() {
		return caseId;
	}

	public String getActivityName() {
		return activityName;
	}
	
	public String getAttributes() {
		return new JSONObject(attributes).toJSONString();
	}
	
	public XesMqttEvent addEventAttribute(String name, String value) {
		return addAttribute(name, value, "event");
	}
	
	public XesMqttEvent addTraceAttribute(String name, String value) {
		return addAttribute(name, value, "trace");
	}

	public XesMqttEvent addAttribute(String name, String value, String type) {
		if (!attributes.containsKey(type)) {
			attributes.put(type, new HashMap<String, String>());
		}
		attributes.get(type).put(name, value);
		return this;
	}

	public XesMqttEvent addAllEventAttributes(XAttributeMap map) {
		return addAllAttributes(map, "event");
	}
	
	public XesMqttEvent addAllTraceAttributes(XAttributeMap map) {
		return addAllAttributes(map, "trace");
	}
	
	public XesMqttEvent addAllAttributes(XAttributeMap map, String type) {
		for (String key : map.keySet()) {
			addAttribute(key, map.get(key).toString(), type);
		}
		return this;
	}
	
	public XesMqttEvent removeTraceAttribute(String name) {
		return removeAttribute(name, "trace");
	}
	
	public XesMqttEvent removeEventAttribute(String name) {
		return removeAttribute(name, "event");
	}
	
	public XesMqttEvent removeAttribute(String name, String type) {
		attributes.get(type).remove(name);
		return this;
	}
}
