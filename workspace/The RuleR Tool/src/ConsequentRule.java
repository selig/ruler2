/**
 * The class to represent Consequent rule
 * 
 * @author mbax2md4
 */
public class ConsequentRule {

	private final String ruleName;
	private final int ruleID;
	private int[] consequentRuleParameterIndexes;
	private String[] consequentRuleParameterStrings;
	
	public ConsequentRule(String name,String[] param) {
		this.ruleID = GlobalFunctions.hashName(name);
		this.ruleName = name;
		this.consequentRuleParameterStrings = param;
	}
	
	public String toString() {
		if(RuleSystem.getRuleName(ruleID) == null)
			return  this.ruleName + "(" + GlobalFunctions.getParameters(consequentRuleParameterStrings) + ")";
		else return  RuleSystem.getRuleName(ruleID) + "(" + GlobalFunctions.getParameters(consequentRuleParameterStrings) + ")";
	}

	public void initializeParameterIndexes(Parameter[] tempParamArray) {
		consequentRuleParameterIndexes = new int[consequentRuleParameterStrings.length];
		
		for(int i=0;i<consequentRuleParameterIndexes.length;i++) {
			consequentRuleParameterIndexes[i] = GlobalFunctions.getParamIndex(consequentRuleParameterStrings[i],tempParamArray);
		}	
	}

}
