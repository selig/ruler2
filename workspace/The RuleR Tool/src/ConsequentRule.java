/**
 * The class to represent Consequent rule
 * 
 * @author mbax2md4
 */
public class ConsequentRule {

	private final String ruleName;
	//private final int ruleID;
	private int[] consequentRuleParameterIndexes;
	private String[] consequentRuleParameterStrings;
	
	public ConsequentRule(String name,String[] param) {
		name = GlobalFunctions.removeWhiteSpaces(name);
		//this.ruleID = GlobalFunctions.hashName(name);
		this.ruleName = name;
		this.consequentRuleParameterStrings = param;
	}
	
	public String toString() {
		if(this.ruleName.toLowerCase().equals("fail") || this.ruleName.toLowerCase().equals("ok"))
			return this.ruleName;
		return  this.ruleName + "(" + GlobalFunctions.getParameters(consequentRuleParameterStrings) + ")";
	}

	public void initializeParameterIndexes(Parameter[] tempParamArray) {
		consequentRuleParameterIndexes = new int[consequentRuleParameterStrings.length];
		
		for(int i=0;i<consequentRuleParameterIndexes.length;i++) {
			consequentRuleParameterIndexes[i] = GlobalFunctions.getParamIndex(consequentRuleParameterStrings[i],tempParamArray);
		}	
	}

	public String getRuleName() {
		return ruleName;
	}

	public int[] getConsequentRuleParameterIndexes() {
		return consequentRuleParameterIndexes;
	}

	public String[] getConsequentRuleParameterStrings() {
		return consequentRuleParameterStrings;
	}
	
	public boolean isFail() {
		return this.ruleName.toLowerCase().equals("fail");
	}

	public boolean isOK() {
		return this.ruleName.toLowerCase().equals("ok");
	}

	public int getConsequentRuleParameterSize() {
		return this.consequentRuleParameterIndexes.length;
	}

}
