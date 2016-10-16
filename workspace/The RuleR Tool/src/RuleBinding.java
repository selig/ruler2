import java.util.ArrayList;

import sun.awt.GlobalCursorManager;


public class RuleBinding {
	
	private final int ruleBindingID;
	private final int ruleID;
	private final int event;
	private final String eventName;
	private String[] eventParameterStrings;
	private int[] eventParameterIndexes;
	private EventCondition[] eventConditions;
	private ArrayList<ConsequentRule> consequentRules;
	
	
	public RuleBinding(int ruleID,String eventName, int event,String[] par, ArrayList<ConsequentRule> consequentRules) {
		this.ruleBindingID = GlobalVariables.getNextBindingCount();
		this.ruleID = ruleID;
		this.event = event;
		this.eventName = eventName;
		this.consequentRules = consequentRules;
		this.eventParameterStrings = par;
	}
	
	public String toString() {
		return eventName + "(" + GlobalFunctions.getParameters(eventParameterStrings) + ")<" + /*eventConditions.toString()*/"-" + "> ¬> " + getConsequentRulesString() + " \n";  
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
			eventParameterIndexes[i] = GlobalFunctions.getParamIndex(eventParameterStrings[i],tempParamArray);
		}
		
		// Initialise event Conditions
		for(EventCondition eventCondition : eventConditions) {
			eventCondition.initializeParameterIndexes(tempParamArray);
		}
		
		// Initialise Consequent Rules
		for(ConsequentRule consequentRule : consequentRules) {
			consequentRule.initializeParameterIndexes(tempParamArray);
		}
	}
	
}
