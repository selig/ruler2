
public class GlobalVariables {
	
	private static int bindingID = 0;
	private static int ruleID = 0;
	private static int consequentRuleID = 0;
	private static int activeRuleSetID = 0;
	
	
	public GlobalVariables() {
		
	}

	public static int getNextBindingCount() {
		bindingID++;
		return bindingID;
	}
	
	public static int getNextRuleID() {
		ruleID++;
		return ruleID;
	}

	public static int getConsequentRuleID() {
		consequentRuleID++;
		return consequentRuleID;
	}

	public static int getNextActiveRuleSetID() {
		activeRuleSetID++;
		return activeRuleSetID;
	}
}
