import java.util.ArrayList;


public class Rule {
	
	private final int ruleID;
	private final String ruleName;
	public enum Modifier {Always, Step, Skip, Fail}
	private final Modifier ruleModifier;
	private String[] ruleParameter;
	private ArrayList<RuleBinding>[] ruleBinding;
	
	
	public Rule(int id,String name,String mod, String parameter) {
		ruleID = id;
		ruleName = name;
		switch(mod) {
		case "Always":
			ruleModifier = Modifier.Always;
		break;
		case "Skip":
			ruleModifier = Modifier.Skip;
			break;
		case "Step":
			ruleModifier = Modifier.Step;
			break;
		default:
			ruleModifier = Modifier.Fail;
		}
		ruleParameter = parameter.trim().split(",");
	}
	
	public String toString() {
		return ruleModifier + " " + ruleName + "("+ getParameters() +")";
	}

	private String getParameters() {
		
		try {
			
			String all = "";
			
			for(String par : ruleParameter) {
				all += par + ", ";
			}
				
			all = all.substring(0, all.length()-2);
			
			return all;
		} catch(Exception e){
			return "";
		}
	}
}
