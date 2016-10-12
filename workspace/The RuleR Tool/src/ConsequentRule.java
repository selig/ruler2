import java.lang.reflect.Array;


public class ConsequentRule {

	private final int consequentRuleID;
	private final String ruleName;
	private final int ruleID;
	private int[] parameters;
	
	public ConsequentRule(String name,int param) {
		
		this.consequentRuleID = GlobalVariables.getConsequentRuleID();
		this.ruleID = GlobalFunctions.hashName(name);
		this.ruleName = name;
		this.parameters = new int[param];
	}
	
	public String toString() {
		if(RuleSystem.getRuleName(ruleID) == null)
			return  this.ruleName + "(" + parameters.length + ")";
		else return  RuleSystem.getRuleName(ruleID) + "(" + parameters.length + ")";
	}

}
