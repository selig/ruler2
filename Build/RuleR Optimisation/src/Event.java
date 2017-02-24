
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
	
	public Event(String newEvent, String Option) {
		
		if(Option.equals("CSV")){
			this.event = newEvent.split(",")[0];
			this.eventParameters = newEvent.split(",",2).length > 1 ? newEvent.split(",",2)[1].split(",") : new String[0];
		} else {
			String eventName = newEvent.split("\\(")[0];
			this.event = eventName;
			
			try{
				String[] eventParameters = newEvent.replaceAll("\\)", "").split("\\(")[1].split(",");
				this.eventParameters = eventParameters;
			} catch(Exception e) {
				this.eventParameters = null;
			}
		}
	}

	public String getEvent() {
		return event;
	}

	public String[] getEventParameters() {
		return eventParameters;
	}
	
	public String[] getEventParameters(Integer[] indexes) {
		if(indexes == null)
			return null;
		
		String[] tempArray = new String[indexes.length];
		
		int count = 0;
		for(int ind : indexes) {
			tempArray[count] = eventParameters[ind];
			count++;
		}
		
		return tempArray;
	}
	
	public String getEventParameter(int index) {
		if(eventParameters == null)
			return null;
		else
			return eventParameters[index];
	}

	public int getEventParametersSize() {
		if(eventParameters == null)
			return 0;
		else
			return eventParameters.length;
	}
	
	public String getEventParams() {
	
		if(eventParameters == null || eventParameters.length == 0)
			return "";
		
		String finalValue = "";
		for(String param : eventParameters) {
			finalValue += param+",";
		}
		
		return GlobalFunctions.subStringLast(finalValue, 1);
	}
	
	public String toString() {
		return this.event + "(" + getEventParams() + ")";
	}

	public Integer getEventId() {
		return GlobalFunctions.hashName(getEvent()+getEventParametersSize());
	}

	public int getEventParametersHashValue(Integer[] matchingIndexes) {
		String[] paramArray = getEventParameters(matchingIndexes);
		String values = GlobalFunctions.getValuesString(paramArray);
		return GlobalFunctions.hashName(values);
	}
}
