
public class Event {
	private final String event;
	private String[] eventParameters;
	
	public Event(String newEvent, String[] newEventParam) {
		this.event = newEvent;
		this.eventParameters = newEventParam;
	}
	
	public Event(String newEvent) {
		
		String eventName = newEvent.split("\\(")[0];
		this.event = eventName;
		
		try{
			String[] eventParameters = newEvent.replaceAll("\\)", "").split("\\(")[1].split(",");
			this.eventParameters = eventParameters;
		} catch(Exception e) {
			this.eventParameters = null;
		}
	}

	public String getEvent() {
		return event;
	}

	public String[] getEventParameters() {
		return eventParameters;
	}
	
	public String getEventParameter(int index) {
		return eventParameters[index];
	}

	public int getEventParametersSize() {
		return eventParameters.length;
	}
	
	public String getEventParams() {
		String finalValue = "";
		for(String param : eventParameters) {
			finalValue += param+",";
		}
		
		return GlobalFunctions.subStringLast(finalValue, 1);
	}
	
	public String toString() {
		return this.event + "(" + getEventParams() + ")";
	}
}
