
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

	public int getEventParametersSize() {
		return eventParameters.length;
	}
}
