import java.util.ArrayList;

import sun.security.action.GetBooleanAction;


public class Rule {
	public enum Modifier {Always, Step, Skip, Fail}
	public enum ExtraModifier {Start, Forbidden, Assert, None}
	
	
	private final int ruleID;
	private final int ruleNameID;
	private final String ruleName;
	private final ExtraModifier extraModifier;
	private final Modifier ruleModifier;
	private String[] ruleParameter;
	private ArrayList<RuleBinding> ruleBinding;
	
	
	public Rule(String name,String mod, String extMod, String parameter) {
		ruleID = GlobalVariables.getNextRuleID();
		ruleName = name;
		ruleNameID = GlobalFunctions.hashName(ruleName);
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
		
		switch(extMod) {
		case "Start":
			extraModifier = ExtraModifier.Start;
		break;
		case "Forbidden":
			extraModifier = ExtraModifier.Forbidden;
			break;
		case "Assert":
			extraModifier = ExtraModifier.Assert;
			break;
		default:
			extraModifier = ExtraModifier.None;
		}
		
		ruleParameter = parameter.trim().split(",");
		ruleBinding = new ArrayList<RuleBinding>();
	}
	
	public String toString() {
		return extraModifier + " " +ruleModifier + " " + ruleName + "("+ getParameters() +") { " + getRuleBindingsString() + " }";
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

	public void addRuleBinding(RuleBinding ruleBinding) {
		this.ruleBinding.add(ruleBinding);
	}
	
	public String getRuleBindingsString() {
		String bindingString = "";
		
		for(RuleBinding binding : ruleBinding) {
			bindingString+= "[" + binding.toString() + "]";
		}
		
		return bindingString;
	}

	public int getRuleID() {
		return ruleID;
	}

	public Integer getRuleNameID() {
		return ruleNameID;
	}

	public String getRuleName() {
		return ruleName;
	}
}
