import java.util.ArrayList;
import java.util.HashMap;


public class Rule {
	
	private static String COMMA = ",";
	
	public enum Modifier {Always, Step, Skip, Fail};
	public enum ExtraModifier {Start, Forbidden, Assert, None};
	
	
	private final int ruleID;
	private final int ruleNameID;
	private final String ruleName;
	private final ExtraModifier extraModifier;
	private final Modifier ruleModifier;
	private final Parameter[] parameters;
	private HashMap<Integer, Variable> ruleVariables;
	private int[] ruleParameterIndexes;
	private String[] ruleParameterStrings;
	private ArrayList<RuleBinding> ruleBinding;
	
	
	public Rule(String name,String mod, String extMod, String parameter, ArrayList<RuleBinding> bindings) {
		ruleID = GlobalVariables.getNextRuleID();
		ruleName = name;
		ruleNameID = GlobalFunctions.hashName(name+parameter);
		ruleModifier = getModifier(mod);		
		extraModifier = getExtraModifier(extMod);
		ruleParameterStrings = GlobalFunctions.removeWhiteSpaces(parameter).split(COMMA);
		ruleBinding = bindings;
		ruleVariables = new HashMap<Integer,Variable>();
		parameters = getParameterArray();
	}
	
	public void addVariable(Variable newVariable) {
		ruleVariables.put(newVariable.getKey(), newVariable);
	}
	
	public boolean containsVariable(String variableName) {
		
		Integer key = new Integer(GlobalFunctions.hashName(variableName));
		
		return ruleVariables.containsKey(key);
	}
	
	public Variable getVariable(Integer key) {
		return ruleVariables.get(key);		
	}
	
	private Parameter[] getParameterArray() {
		
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		
		// Get different parameters from Rule
		for(String param : ruleParameterStrings) {
			parameters.add(new Parameter(param, this));
		}
		
		// Get different parameters from Event Parameters
		for(RuleBinding ruleBind : ruleBinding) {
			String[] allParameters;
			
			if((allParameters = ruleBind.getEventParamArray()) != null) {
			
				for(String param : allParameters) {
					if(GlobalFunctions.exists(param, parameters)) continue;
					else parameters.add(new Parameter(param, this));
				}
				
				// Get different parameters from Conditions
				for(Condition condition : ruleBind.getEventConditions()) {
					
					String[] allconditions;
					
					if((allconditions = condition.getParameterArray()) != null)
						for(String param : allconditions) {
							System.out.println("-- --> Parameter : " + param);
							if(GlobalFunctions.exists(param, parameters)) continue;
							else parameters.add(new Parameter(param, this));
						}
				}
				
				// Get different parameters from Consequent Rule Parameters
			}
		}
		
		
		int numOfParameters = parameters.size();
		
		//System.out.println("--> There are " + numOfParameters + " in this rule");
		
		Parameter[] tempParamArray = new Parameter[numOfParameters];
		
		for(int i=0;i<numOfParameters;i++) {
			tempParamArray[i] = parameters.get(i);
		}
		
		ruleParameterIndexes = new int[ruleParameterStrings.length];
		
		for(int i=0; i< ruleParameterIndexes.length;i++) {
			ruleParameterIndexes[i] = GlobalFunctions.getParamIndex(ruleParameterStrings[i],tempParamArray);
		}
		
		for(RuleBinding ruleBind : ruleBinding) {
			ruleBind.initializeParameterIndexes(tempParamArray);
		}
		
		return tempParamArray;
	}

	public Parameter getParameter(int index) {
		return parameters[index];
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
	
	public int[] getRuleParameterIndexes() {
		return ruleParameterIndexes;
	}
	
	public ExtraModifier getExtraModifier() {
		return extraModifier;
	}

	public Modifier getRuleModifier() {
		return ruleModifier;
	}
	
	public ArrayList<RuleBinding> getRuleBinding() {
		return ruleBinding;
	}

	public String toString() {
		return extraModifier + " " +ruleModifier + " " + ruleName + "("+ GlobalFunctions.getParameters(ruleParameterStrings) +") { " + getRuleBindingsString() + " }";
	}

}
