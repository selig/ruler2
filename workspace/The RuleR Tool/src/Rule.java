import java.util.ArrayList;

import sun.font.CreatedFontTracker;
import sun.security.action.GetBooleanAction;


public class Rule {
	
	private static String COMMA = ",";
	
	public enum Modifier {Always, Step, Skip, Fail}
	public enum ExtraModifier {Start, Forbidden, Assert, None}
	
	
	private final int ruleID;
	private final int ruleNameID;
	private final String ruleName;
	private final ExtraModifier extraModifier;
	private final Modifier ruleModifier;
	private final Parameter[] parameterArray;
	private int[] ruleParameterIndexes;
	private String[] ruleParameter;
	private ArrayList<RuleBinding> ruleBinding;
	
	
	public Rule(String name,String mod, String extMod, String parameter) {
		ruleID = GlobalVariables.getNextRuleID();
		ruleName = name;
		ruleNameID = GlobalFunctions.hashName(ruleName);
		ruleModifier = getModifier(mod);		
		extraModifier = getExtraModifier(extMod);
		ruleParameter = parameter.trim().split(COMMA);
		ruleBinding = new ArrayList<RuleBinding>();
		parameterArray = getParameterArray();
	}
	
	private Parameter[] getParameterArray() {
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		
		for(String param : ruleParameter) {
			parameters.add(new Parameter(param));
		}
	
		for(RuleBinding ruleBind : ruleBinding) {
			for(String param : ruleBind.getEventParamArray()) {
				if(GlobalFunctions.exists(param, ruleParameter)) continue;
				else parameters.add(new Parameter(param));
			}
		}
		
		int numOfParameters = parameters.size();
		
		Parameter[] tempParamArray = new Parameter[numOfParameters];
		
		for(int i=0;i<numOfParameters;i++) {
			tempParamArray[i] = parameters.get(i);
		}
		
		ruleParameterIndexes = new int[ruleParameter.length];
		
		for(int i=0; i< ruleParameterIndexes.length;i++) {
			ruleParameterIndexes[i] = GlobalFunctions.getParamIndex(ruleParameter[i],tempParamArray);
		}
		
		for(RuleBinding ruleBind : ruleBinding) {
			ruleBind.initializeParameterIndexes(tempParamArray);
		}
		
		return tempParamArray;
	}

	public String toString() {
		return extraModifier + " " +ruleModifier + " " + ruleName + "("+ GlobalFunctions.getParameters(ruleParameter) +") { " + getRuleBindingsString() + " }";
	}

	private ExtraModifier getExtraModifier(String extMod) {
		
		switch(extMod) {
		case "Start":
			return ExtraModifier.Start;
		case "Forbidden":
			return ExtraModifier.Forbidden;
		case "Assert":
			return ExtraModifier.Assert;
		default:
			return ExtraModifier.None;
		}
	}
	
	private Modifier getModifier(String mod) {
		switch(mod) {
		case "Always":
			return Modifier.Always;
		case "Skip":
			return Modifier.Skip;
		case "Step":
			return Modifier.Step;
		default:
			return Modifier.Fail;
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
