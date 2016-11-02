
public class Event {
	private final String event;
	private final String[] eventParameters;
	
	public Event(String newEvent, String[] newEventParam) {
		this.event = newEvent;
		this.eventParameters = newEventParam;
	}

	public String getEvent() {
		return event;
	}

	public String[] getEventParameters() {
		return eventParameters;
	}
}
