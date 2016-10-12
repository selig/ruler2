import java.util.ArrayList;

import sun.awt.GlobalCursorManager;


public class RuleBinding {
	
	private final int ruleBindingID;
	private final int ruleID;
	private final int event;
	private final String eventName;
	private int[] eventParameters;
	private EventCondition[] eventConditions;
	private ArrayList<ConsequentRule> consequentRules;
	
	
	public RuleBinding(int ruleID,String eventName, int event,int par, ArrayList<ConsequentRule> consequentRules) {
		this.ruleBindingID = GlobalVariables.getNextBindingCount();
		this.ruleID = ruleID;
		this.event = event;
		this.eventName = eventName;
		this.consequentRules = consequentRules;
		this.eventParameters = new int[par];
	}
	
	public String toString() {
		return eventName + "(" + eventParameters.length + ")<" + /*eventConditions.toString()*/"-" + "> ¬> " + getConsequentRulesString() + " \n";  
	}

	private String getConsequentRulesString() {
		String consequentRuleString = "";
		
		for(ConsequentRule rule : consequentRules) {
			consequentRuleString += rule.toString() + ", ";
		}
		
		consequentRuleString = consequentRuleString.substring(0, consequentRuleString.length()-2);
		
		return consequentRuleString;
	}
	
}
