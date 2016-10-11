import java.util.ArrayList;


public class Rule {
	
	private final int ruleID;
	private final String ruleName;
	private final int ruleModifier;
	private Object[] ruleParameter;
	private ArrayList<Rule_Binding>[] ruleBinding;
	
	
	public Rule(int id,String name,int modifier) {
		ruleID = id;
		ruleName = name;
		ruleModifier = modifier;
	}
	
	public String toString() {
		return ruleModifierString(ruleModifier) + " " + ruleName;
	}

	private String ruleModifierString(int modifier) {
		
		switch (modifier) {
		case 1:
			return "always";
		case 2:
			return "state";
		case 3:
			return "step";
		default:
			return "unknown";
		}
		
	}
	
	public static int modifierToInt(String modifier) {
		switch (modifier) {
		case "always":
			return 1;
		case "state":
			return 2;
		case "step":
			return 3;
		case "":
			return -1;
		default:
			return 0;
		}		
	}
	

}
