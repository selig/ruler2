import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RuleBinding {
	
	private final int event;
	private final String eventName;
	private String[] eventParameterStrings;
	private int[] eventParameterIndexes;
	//integer 1 - parameter index in Big Array of Rule parameter
	//integer 2 - parameter index in event Description
	private Map<Integer, Integer> eventParameters;
	private Condition[] eventConditions;
	private ArrayList<ConsequentRule> consequentRules;
	
	
	public RuleBinding(String eventName,String[] par, String[] conditions, ArrayList<ConsequentRule> consequentRules) {
		this.event = GlobalFunctions.hashName(eventName);
		this.eventName = eventName;
		this.eventParameterStrings = par;
		this.eventParameters = new HashMap<Integer, Integer>();
		this.eventConditions = createConditions(conditions);
		this.consequentRules = consequentRules;
	}

	public boolean isThisEvent(String newEventName) {
		return this.event == GlobalFunctions.hashName(newEventName);
	}
	
	private String getConsequentRulesString() {
		String consequentRuleString = "";
		
		for(ConsequentRule rule : consequentRules) {
			consequentRuleString += rule.toString() + ", ";
		}
		
		consequentRuleString = consequentRuleString.substring(0, consequentRuleString.length()-2);
		
		return consequentRuleString;
	}
	
	public String[] getEventParamArray() {
		return eventParameterStrings;
	}

	public void initializeParameterIndexes(Parameter[] tempParamArray) {
		eventParameterIndexes = new int[eventParameterStrings.length];
		
		for(int i=0;i<eventParameterIndexes.length;i++) {
			int parameterIndex = GlobalFunctions.getParamIndex(eventParameterStrings[i],tempParamArray);
			eventParameterIndexes[i] = parameterIndex;
			eventParameters.put(parameterIndex,i);
		}
		
		// Initialise event Conditions
		for(Condition eventCondition : eventConditions) {
			eventCondition.initializeParameterIndexes(tempParamArray);
		}
		
		// Initialise Consequent Rules
		for(ConsequentRule consequentRule : consequentRules) {
			consequentRule.initializeParameterIndexes(tempParamArray);
		}
	}
	
	private Condition[] createConditions(String[] conditions) {
		
		if(conditions == null) return null;
		
		Condition[] tempConditionArrayConditions = new Condition[conditions.length];
		
		int i = 0;
		
		for(String condition : conditions) {
			tempConditionArrayConditions[i] = new Condition(condition);
			i++;
		}
		
		return tempConditionArrayConditions;
	}
	
	private String getEventConditionsString() {
		String finalString = "";
		
		for(Condition condition : eventConditions) {
			finalString += condition.toString() + ", ";
		}
		
		finalString = GlobalFunctions.subStringLast(finalString,2);

		return finalString;
	}
	
	public Condition[] getEventConditions() {
		return this.eventConditions;
	}
	
	public String getEventName() {
		return eventName;
	}

	public ArrayList<ConsequentRule> getConsequentRules() {
		return consequentRules;
	}

	public int[] getEventParameterIndexes() {
		return eventParameterIndexes;
	}
	
	public int getEventParameterIndex(int key){
		return eventParameters.get(key);
	}

	public String toString() {
		return eventName + "(" + GlobalFunctions.getParameters(eventParameterStrings) + ")<" + getEventConditionsString() + "> ¬> " + getConsequentRulesString();
	}
}
